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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A type-safe event bus for publishing and subscribing to events.
 *
 * The [EventBus] uses Kotlin Coroutines and [SharedFlow] to manage event distribution.
 * It supports different buffering strategies, replaying events to new subscribers,
 * and asynchronous event processing.
 *
 * @param E The base type of events handled by this bus. Must extend [EventMarker].
 * @since 1.0.1
 */
public class EventBus<E : EventMarker> internal constructor(private val data: EventBusData) : Disposable {
    private val isDisposed = atomic(false)

    private val sentEvents = MutableSharedFlow<E>(
        replay = data.replay,
        extraBufferCapacity = data.extraBufferCapacity,
        onBufferOverflow = data.overflowStrategy
    )

    /**
     * Pushes an event to the bus asynchronously.
     *
     * The event is emitted within the bus's coroutine scope. If the bus is
     * disposed, the event is ignored.
     *
     * @param event The event to push.
     * @since 1.0.1
     */
    public fun push(event: E) {
        if (isDisposed.value) return
        data.scope.launch {
            sentEvents.emit(event)
        }
    }

    /**
     * Emits an event to the bus and suspends until it is delivered.
     *
     * If the bus is disposed, the event is ignored.
     *
     * @param event The event to emit.
     * @since 1.0.1
     */
    public suspend fun emit(event: E) {
        if (isDisposed.value) return
        sentEvents.emit(event)
    }

    /**
     * Attempts to push an event to the bus immediately without suspending.
     *
     * @param event The event to push.
     * @return `true` if the event was successfully buffered, `false` otherwise.
     * @since 1.0.1
     */
    public fun tryPush(event: E): Boolean {
        if (isDisposed.value) {
            return false
        }

        return sentEvents.tryEmit(event)
    }

    /**
     * Pushes multiple events to the bus asynchronously.
     *
     * Each event is emitted within the bus's coroutine scope.
     *
     * @param events The collection of events to push.
     * @since 1.0.1
     */
    public fun pushAll(events: Iterable<E>) {
        data.scope.launch {
            events.forEach { emit(it) }
        }
    }

    /**
     * Pushes multiple events to the bus asynchronously.
     *
     * @param events The events to push.
     * @since 1.0.1
     */
    public fun pushAll(vararg events: E) {
        pushAll(events.asIterable())
    }

    /**
     * Emits multiple events to the bus and suspends until all are delivered.
     *
     * @param events The collection of events to emit.
     * @since 1.0.1
     */
    public suspend fun emitAll(events: Iterable<E>) {
        events.forEach { emit(it) }
    }

    /**
     * Emits multiple events to the bus and suspends until all are delivered.
     *
     * @param events The events to emit.
     * @since 1.0.1
     */
    public suspend fun emitAll(vararg events: E) {
        emitAll(events.asIterable())
    }

    /**
     * Attempts to push multiple events to the bus immediately.
     *
     * @param events The collection of events to push.
     * @return `true` if all events were successfully buffered, `false` otherwise.
     * @since 1.0.1
     */
    public fun tryPushAll(events: Iterable<E>): Boolean {
        return events.all { tryPush(it) }
    }

    /**
     * Attempts to push multiple events to the bus immediately.
     *
     * @param events The events to push.
     * @return `true` if all events were successfully buffered, `false` otherwise.
     * @since 1.0.1
     */
    public fun tryPushAll(vararg events: E): Boolean {
        return tryPushAll(events.asIterable())
    }

    /**
     * Pushes an event to the bus asynchronously only if the given predicate matches.
     *
     * The event is emitted within the bus's coroutine scope. If the condition is not met,
     * the event is ignored.
     *
     * @param event The event to push.
     * @param predicate The condition that must be met for the event to be pushed.
     * @since 1.1.0
     */
    public fun pushIf(event: E, predicate: (E) -> Boolean): Unit = if (predicate(event)) push(event) else Unit

    /**
     * Emits an event after a specified delay.
     *
     * @param event The event to emit.
     * @param delay The duration to wait before emitting.
     * @since 1.0.1
     */
    public suspend fun emitDelayed(
        event: E,
        delay: Duration
    ) {
        delay(delay)
        emit(event)
    }

    /**
     * Emits an event after a specified delay in milliseconds.
     *
     * @param event The event to emit.
     * @param delayMillisecond The time in milliseconds to wait before emitting.
     * @since 1.0.1
     */
    public suspend fun emitDelayed(
        event: E,
        delayMillisecond: Long
    ) {
        emitDelayed(event, delayMillisecond.milliseconds)
    }

    /**
     * Emits an event to the bus and suspends until it is delivered, only if the given predicate matches.
     *
     * If the condition is not met, the event is ignored.
     *
     * @param event The event to emit.
     * @param predicate The condition that must be met for the event to be emitted.
     * @since 1.1.0
     */
    public suspend fun emitIf(event: E, predicate: suspend (E) -> Boolean): Unit =
        if (predicate(event)) emit(event) else Unit

    /**
     * Returns a [SharedFlow] of all events published to this bus.
     *
     * @return A flow of events.
     * @since 1.0.1
     */
    public fun events(): SharedFlow<E> = sentEvents.asSharedFlow()

    /**
     * Subscribes to events of a specific type.
     *
     * @param T The type of event to subscribe to.
     * @param eventType The class of the event type.
     * @param onError Optional error handler for this subscription. If null, the
     * bus's global error handler is used.
     * @param on The block to execute when an event is received.
     * @return A [Job] representing the subscription. Cancel this job to unsubscribe.
     * @since 1.0.1
     */
    public fun <T : EventMarker> subscribe(
        eventType: KClass<T>,
        onError: (suspend (Throwable) -> Unit)? = null,
        on: suspend (T) -> Unit
    ): Job {
        return events()
            .filter { eventType.isInstance(it) }
            .map { eventType.cast(it) }
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
     * Subscribes to events of a specific type using a reified type parameter.
     *
     * @param T The type of event to subscribe to.
     * @param onError Optional error handler for this subscription. If null, the
     * bus's global error handler is used.
     * @param on The block to execute when an event is received.
     * @return A [Job] representing the subscription. Cancel this job to unsubscribe.
     * @since 1.0.1
     */
    public inline fun <reified T : EventMarker> subscribe(
        noinline onError: (suspend (Throwable) -> Unit)? = null,
        noinline on: suspend (T) -> Unit
    ): Job {
        return subscribe(T::class, onError, on)
    }

    /**
     * Disposes of the event bus and cancels its coroutine scope.
     *
     * After disposal, the bus will no longer accept or distribute events.
     *
     * @since 1.0.1
     */
    override fun dispose() {
        if (isDisposed.value) return
        if (isDisposed.compareAndSet(expect = false, update = true)) {
            data.scope.cancel()
        }
    }
}

/**
 * Creates and configures a new [EventBus] within the given scope.
 *
 * @param E The base event type for the bus.
 * @param scope The [CoroutineScope] in which the bus will operate.
 * @param builder A lambda for configuring the bus via [EventBusBuilder].
 * @return A configured [EventBus] instance.
 * @since 1.0.1
 */
public fun <E : EventMarker> eventBus(scope: CoroutineScope, builder: EventBusBuilder.() -> Unit = {}): EventBus<E> {
    val eventBusBuilder = EventBusBuilder(scope)
    eventBusBuilder.builder()
    val data = eventBusBuilder.produce()
    return EventBus(data)
}
