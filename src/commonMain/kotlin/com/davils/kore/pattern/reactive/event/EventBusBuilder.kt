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

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.pattern.creational.dsl.Dsl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlin.reflect.KClass

/**
 * Builder for creating and configuring an [EventBus].
 *
 * This class exposes a Kafka-inspired DSL for declaring named, strongly typed
 * topics upfront. Each declared topic maps to an independent [EventTopic] with its
 * own configuration and supervised coroutine scope.
 *
 * Topic names must be unique within a single builder, and at least one topic
 * must be declared before the bus can be produced. Instances of this builder
 * are not intended to be reused across multiple bus creations.
 *
 * @since 1.1.0
 */
@KoreDsl
public class EventBusBuilder internal constructor(private val scope: CoroutineScope) : Dsl<EventBusData> {
    private val topics: MutableMap<String, EventTopic<*>> = mutableMapOf()

    /**
     * The bus-wide default number of events replayed to new subscribers.
     *
     * Applied to every topic that does not override [EventTopicBuilder.replay]
     * inside its own DSL block. Must be non-negative. Defaults to `0`.
     *
     * @since 1.1.0
     */
    public var replay: Int = 0

    /**
     * The bus-wide default additional buffer capacity beyond [replay].
     *
     * Applied to every topic that does not override
     * [EventTopicBuilder.extraBufferCapacity]. Must be non-negative. Defaults
     * to `64`.
     *
     * @since 1.1.0
     */
    public var extraBufferCapacity: Int = 64

    /**
     * The bus-wide default strategy applied when a topic buffer is full.
     *
     * Applied to every topic that does not override
     * [EventTopicBuilder.overflowStrategy]. Defaults to
     * [BufferOverflow.DROP_OLDEST].
     *
     * @since 1.1.0
     */
    public var overflowStrategy: BufferOverflow = BufferOverflow.DROP_OLDEST

    /**
     * The bus-wide default error handler for topic subscription failures.
     *
     * Applied to every topic that does not override [EventTopicBuilder.onError].
     * Defaults to a no-op.
     *
     * @since 1.1.0
     */
    public var onError: suspend (Throwable) -> Unit = {}

    /**
     * Declares a strongly typed topic on the bus.
     *
     * The provided [name] must be unique within this builder. The [eventType]
     * describes the runtime class of the events flowing through the topic and
     * is stored on the [EventTopic] to support type-safe lookup on the produced
     * [EventBus]. The [builder] lambda configures the topic's underlying
     * reactive parameters.
     *
     * @param T The event type carried by the declared topic. Must extend [EventMarker].
     * @param name The unique name identifying the topic within the bus.
     * @param eventType The runtime class of the topic's event type.
     * @param builder An optional lambda configuring the topic's [EventTopicBuilder].
     * @return The registered [EventTopic] handle, ready to publish and subscribe on.
     * @throws IllegalArgumentException If a topic with the same [name] has already been declared.
     * @since 1.1.1
     */
    public fun <T : EventMarker> topic(
        name: String,
        eventType: KClass<T>,
        builder: EventTopicBuilder.() -> Unit = {}
    ): EventTopic<T> {
        require(!topics.containsKey(name)) { "Topic '$name' is already declared on this bus" }
        val topicBuilder = EventTopicBuilder(scope, currentDefaults())
        topicBuilder.builder()
        val topic = EventTopic(name, eventType, topicBuilder.produce())
        topics[name] = topic
        return topic
    }

    /**
     * Declares a strongly typed topic on the bus using a reified type parameter.
     *
     * Convenience overload of [topic] that infers the [KClass] from the reified
     * type argument.
     *
     * @param T The event type carried by the declared topic. Must extend [EventMarker].
     * @param name The unique name identifying the topic within the bus.
     * @param builder An optional lambda configuring the topic's [EventTopicBuilder].
     * @return The registered [EventTopic] handle, ready to publish and subscribe on.
     * @throws IllegalArgumentException If a topic with the same [name] has already been declared.
     * @since 1.1.1
     */
    public inline fun <reified T : EventMarker> topic(
        name: String,
        noinline builder: EventTopicBuilder.() -> Unit = {}
    ): EventTopic<T> = topic(name, T::class, builder)

    private fun currentDefaults(): EventBusDefaults = EventBusDefaults(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
        overflowStrategy = overflowStrategy,
        onError = onError
    )

    /**
     * Produces the fully configured [EventBus].
     *
     * All topics declared through [topic] are frozen into an immutable map and
     * handed off to the resulting bus. Called internally by the [eventBus] DSL
     * entry point.
     *
     * @return An [EventBusData] snapshot containing every declared topic, ready to be wrapped in an [EventBus].
     * @throws IllegalStateException If no topic has been declared.
     * @since 1.1.1
     */
    override fun produce(): EventBusData {
        check(topics.isNotEmpty()) { "An EventBus must declare at least one topic" }
        return EventBusData(topics = topics.toMap())
    }
}
