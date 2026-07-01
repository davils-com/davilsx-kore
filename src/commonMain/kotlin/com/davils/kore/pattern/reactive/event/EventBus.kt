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
import kotlin.reflect.KClass

/**
 * A multi-topic, Kafka-inspired event bus.
 *
 * The [EventBus] owns one or more strongly typed [EventTopic] channels. Topics are
 * declared upfront through the [eventBus] DSL and remain immutable for the
 * lifetime of the bus. They can be retrieved at any time by name and event
 * type through [topic], which validates the declared event type at runtime.
 *
 * The bus implements [Disposable]. Disposing the bus disposes every topic in
 * a single atomic step, cancelling all active subscriptions and rejecting
 * further publications.
 *
 * @since 1.1.0
 */
public class EventBus internal constructor(private val data: EventBusData) : Disposable {
    private val topics: Map<String, EventTopic<*>> get() = data.topics
    private val isDisposed = atomic(false)

    /**
     * Retrieves a strongly typed [EventTopic] handle by name and event type.
     *
     * The topic is located by [name] and validated against [eventType] at
     * runtime. If both match the declaration made in the DSL, the handle is
     * returned with its generic parameter narrowed to [T].
     *
     * The narrowing relies on an unchecked cast guarded by the runtime
     * [KClass] equality check; the operation is safe as long as topics are
     * only ever created through the [eventBus] DSL, which associates each
     * [EventTopic] instance with its declared [KClass].
     *
     * @param T The event type of the topic to retrieve. Must extend [EventMarker].
     * @param name The unique name of the topic as declared in the DSL.
     * @param eventType The runtime class of the topic's event type.
     * @return The [EventTopic] handle associated with [name] and [eventType].
     * @throws IllegalArgumentException If no topic with the given name is registered,
     * or if the topic's declared event type does not match [eventType].
     * @since 1.1.1
     */
    @Suppress("UNCHECKED_CAST")
    public fun <T : EventMarker> topic(name: String, eventType: KClass<T>): EventTopic<T> {
        val topic = topics[name]
            ?: throw IllegalArgumentException("Topic '$name' is not registered on this bus")
        require(topic.eventType == eventType) {
            "Topic '$name' is declared with event type ${topic.eventType} but was requested as $eventType"
        }
        return topic as EventTopic<T>
    }

    /**
     * Retrieves a strongly typed [EventTopic] handle using a reified type parameter.
     *
     * Convenience overload of [topic] that infers the [KClass] from the reified
     * type argument.
     *
     * @param T The event type of the topic to retrieve. Must extend [EventMarker].
     * @param name The unique name of the topic as declared in the DSL.
     * @return The [EventTopic] handle associated with [name] and the reified type [T].
     * @throws IllegalArgumentException If no topic with the given name is registered,
     * or if the topic's declared event type does not match [T].
     * @since 1.1.1
     */
    public inline fun <reified T : EventMarker> topic(name: String): EventTopic<T> = topic(name, T::class)

    /**
     * Returns the names of every topic registered on this bus.
     *
     * The returned set is an immutable snapshot and is intended primarily for
     * diagnostics and logging.
     *
     * @return An immutable set containing every declared topic name.
     * @since 1.1.1
     */
    public fun topicNames(): Set<String> = topics.keys.toSet()

    /**
     * Disposes the bus and every topic it owns.
     *
     * After disposal, every topic scope is cancelled and any further publish
     * attempts on the contained topics are ignored. Repeated calls to
     * [dispose] are no-ops.
     *
     * @since 1.1.0
     */
    override fun dispose() {
        if (isDisposed.value) return
        if (isDisposed.compareAndSet(expect = false, update = true)) {
            topics.values.forEach { it.dispose() }
        }
    }
}

/**
 * Creates and configures a new [EventBus] using a type-safe DSL.
 *
 * The DSL requires the caller to declare at least one topic. Each topic is
 * independent and receives its own reactive configuration and supervised
 * child scope. Once produced, the topic set is immutable and topics can be
 * retrieved either via the [EventTopic] handle returned by the DSL or later via
 * [EventBus.topic].
 *
 * Example:
 * ```
 * val bus = eventBus(scope) {
 *     topic<UserEvent>("users") {
 *         replay = 10
 *     }
 *     topic<OrderEvent>("orders")
 * }
 * bus.topic<UserEvent>("users").subscribe { println(it) }
 * ```
 *
 * @param scope The [CoroutineScope] in which every topic's operations run.
 * @param builder A lambda configuring the bus through [EventBusBuilder].
 * @return A fully configured [EventBus] with every declared topic ready to use.
 * @throws IllegalStateException If the DSL block does not declare any topic.
 * @since 1.1.0
 */
public fun eventBus(scope: CoroutineScope, builder: EventBusBuilder.() -> Unit): EventBus {
    val eventBusBuilder = EventBusBuilder(scope)
    eventBusBuilder.builder()
    return EventBus(eventBusBuilder.produce())
}
