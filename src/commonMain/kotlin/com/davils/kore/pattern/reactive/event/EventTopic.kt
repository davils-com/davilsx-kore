/*
 * Copyright 2026 Davils
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.davils.kore.pattern.reactive.event

import com.davils.kore.pattern.functional.loan.Disposable
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A single, strongly typed channel inside an [EventBus].
 *
 * A [EventTopic] represents a Kafka-like named stream: it owns its own reactive
 * buffer, its own supervised coroutine scope, and its own set of subscribers.
 * Events published on one topic are never observed by subscribers of another
 * topic on the same bus, even if their event types are compatible.
 *
 * Topics are created exclusively through the [EventBusBuilder.topic] DSL and
 * are immutable once the bus has been produced. Disposing the owning bus also
 * disposes every topic.
 *
 * @param T The event type carried by this topic. Must extend [EventMarker].
 * @since 1.1.0
 */
public class EventTopic<T : EventMarker> internal constructor(
    /**
     * The unique name identifying this topic within its owning [EventBus].
     *
     * Assigned at construction time and immutable for the lifetime of the topic.
     * Must be unique across all topics of the same bus.
     *
     * @since 1.1.0
     */
    public val name: String,
    /**
     * The runtime class of the events carried by this topic.
     *
     * Used to perform safe casts on values flowing through the shared underlying
     * event stream, avoiding unchecked casts on the caller side.
     *
     * @since 1.1.0
     */
    public val eventType: KClass<T>,
    private val data: EventTopicData
) : Disposable {
    private val isDisposed = atomic(false)
    private val sentEvents = MutableSharedFlow<T>(
        replay = data.replay,
        extraBufferCapacity = data.extraBufferCapacity,
        onBufferOverflow = data.overflowStrategy
    )

    /**
     * Publishes an event to the topic asynchronously.
     *
     * The event is emitted within the topic's coroutine scope. If the topic
     * has been disposed, the call is silently ignored.
     *
     * @param event The event to publish.
     * @since 1.1.0
     */
    public fun push(event: T) {
        if (isDisposed.value) return
        data.scope.launch {
            sentEvents.emit(event)
        }
    }

    /**
     * Publishes an event to the topic and suspends until it is delivered.
     *
     * If the topic has been disposed, the call is silently ignored.
     *
     * @param event The event to publish.
     * @since 1.1.0
     */
    public suspend fun emit(event: T) {
        if (isDisposed.value) return
        sentEvents.emit(event)
    }

    /**
     * Attempts to publish an event to the topic immediately without suspending.
     *
     * @param event The event to publish.
     * @return `true` if the event was successfully buffered, `false` otherwise.
     * @since 1.1.0
     */
    public fun tryPush(event: T): Boolean {
        if (isDisposed.value) return false
        return sentEvents.tryEmit(event)
    }

    /**
     * Publishes multiple events to the topic asynchronously.
     *
     * Each event is emitted within the topic's coroutine scope.
     *
     * @param events The collection of events to publish.
     * @since 1.1.0
     */
    public fun pushAll(events: Iterable<T>) {
        data.scope.launch {
            events.forEach { emit(it) }
        }
    }

    /**
     * Publishes multiple events to the topic asynchronously.
     *
     * @param events The events to publish.
     * @since 1.1.0
     */
    public fun pushAll(vararg events: T) {
        pushAll(events.asIterable())
    }

    /**
     * Publishes multiple events to the topic and suspends until all are delivered.
     *
     * @param events The collection of events to emit.
     * @since 1.1.0
     */
    public suspend fun emitAll(events: Iterable<T>) {
        events.forEach { emit(it) }
    }

    /**
     * Publishes multiple events to the topic and suspends until all are delivered.
     *
     * @param events The events to emit.
     * @since 1.1.0
     */
    public suspend fun emitAll(vararg events: T) {
        emitAll(events.asIterable())
    }

    /**
     * Attempts to publish multiple events to the topic immediately.
     *
     * @param events The collection of events to publish.
     * @return `true` if every event was successfully buffered, `false` otherwise.
     * @since 1.1.0
     */
    public fun tryPushAll(events: Iterable<T>): Boolean = events.all { tryPush(it) }

    /**
     * Attempts to publish multiple events to the topic immediately.
     *
     * @param events The events to publish.
     * @return `true` if every event was successfully buffered, `false` otherwise.
     * @since 1.1.0
     */
    public fun tryPushAll(vararg events: T): Boolean = tryPushAll(events.asIterable())

    /**
     * Publishes an event asynchronously only when the given predicate matches.
     *
     * If the predicate returns `false`, the event is dropped without any side
     * effects.
     *
     * @param event The event to publish.
     * @param predicate The condition that must hold for the event to be published.
     * @since 1.1.0
     */
    public fun pushIf(event: T, predicate: (T) -> Boolean): Unit =
        if (predicate(event)) push(event) else Unit

    /**
     * Publishes an event after a specified delay.
     *
     * @param event The event to emit.
     * @param delay The duration to wait before emitting.
     * @since 1.1.0
     */
    public suspend fun emitDelayed(event: T, delay: Duration) {
        delay(delay)
        emit(event)
    }

    /**
     * Publishes an event after a specified delay in milliseconds.
     *
     * @param event The event to emit.
     * @param delayMillisecond The time in milliseconds to wait before emitting.
     * @since 1.1.0
     */
    public suspend fun emitDelayed(event: T, delayMillisecond: Long) {
        emitDelayed(event, delayMillisecond.milliseconds)
    }

    /**
     * Publishes an event with suspension only when the given predicate matches.
     *
     * If the predicate returns `false`, the event is dropped.
     *
     * @param event The event to emit.
     * @param predicate The condition that must hold for the event to be emitted.
     * @since 1.1.0
     */
    public suspend fun emitIf(event: T, predicate: suspend (T) -> Boolean): Unit =
        if (predicate(event)) emit(event) else Unit

    /**
     * Returns a [SharedFlow] of every event published to this topic.
     *
     * The returned flow is scoped strictly to this topic and never observes
     * events published to sibling topics.
     *
     * @return A flow of events emitted on this topic.
     * @since 1.1.0
     */
    public fun events(): SharedFlow<T> = sentEvents.asSharedFlow()

    /**
     * Subscribes to every event published to this topic.
     *
     * The provided suspending block is invoked for each event. Cancel the
     * returned [Job] to unsubscribe.
     *
     * @param onError Optional error handler invoked when the subscription block
     * throws. If null, the topic's global error handler is used.
     * @param on The suspending block executed for each received event.
     * @return A [Job] representing the active subscription.
     * @since 1.1.0
     */
    public fun subscribe(
        onError: (suspend (Throwable) -> Unit)? = null,
        on: suspend (T) -> Unit
    ): Job {
        return events()
            .onEach {
                try {
                    on(it)
                } catch (e: Throwable) {
                    onError?.invoke(e) ?: data.onError(e)
                }
            }
            .launchIn(data.scope)
    }

    /**
     * Disposes the topic and cancels its coroutine scope.
     *
     * After disposal, all active subscriptions are cancelled and any further
     * publish attempts are ignored. Repeated calls to [dispose] are no-ops.
     *
     * @since 1.1.0
     */
    override fun dispose() {
        if (isDisposed.value) return
        if (isDisposed.compareAndSet(expect = false, update = true)) {
            data.scope.cancel()
        }
    }
}
