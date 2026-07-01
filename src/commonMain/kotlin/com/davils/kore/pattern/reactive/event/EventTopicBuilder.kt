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
import com.davils.kore.pattern.creational.dsl.validation.DslValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.plus

/**
 * Builder for configuring a single [EventTopic] within an [EventBus].
 *
 * This DSL is invoked from `EventBusBuilder.topic` and exposes the reactive
 * parameters (replay, buffer, overflow strategy, error handling) that back
 * the topic's underlying `SharedFlow`. Each topic receives its own supervised
 * child scope derived from the bus scope so that failures in one topic never
 * cascade into siblings.
 *
 * @since 1.1.1
 */
@KoreDsl
public class EventTopicBuilder internal constructor(
    private val scope: CoroutineScope,
    defaults: EventBusDefaults = EventBusDefaults()
) : DslValidator<EventTopicData>() {
    /**
     * The number of events replayed to new subscribers of the topic.
     *
     * Must be non-negative. Defaults to the value inherited from the enclosing
     * [EventBusBuilder] (globally 0 if not overridden).
     *
     * @since 1.1.1
     */
    public var replay: Int = defaults.replay

    /**
     * The additional buffer capacity beyond the [replay] count.
     *
     * Must be non-negative. Defaults to the value inherited from the enclosing
     * [EventBusBuilder] (globally 64 if not overridden).
     *
     * @since 1.1.1
     */
    public var extraBufferCapacity: Int = defaults.extraBufferCapacity

    /**
     * The strategy applied when the topic buffer is full.
     *
     * Defaults to the value inherited from the enclosing [EventBusBuilder]
     * (globally [BufferOverflow.DROP_OLDEST] if not overridden).
     *
     * @since 1.1.1
     */
    public var overflowStrategy: BufferOverflow = defaults.overflowStrategy

    /**
     * The default error handler for subscription failures on this topic.
     *
     * Invoked when a subscription block throws and does not provide its own
     * error handler. Defaults to the handler inherited from the enclosing
     * [EventBusBuilder] (globally a no-op if not overridden).
     *
     * @since 1.1.1
     */
    public var onError: suspend (Throwable) -> Unit = defaults.onError

    override fun data(): EventTopicData {
        val topicScope = scope + SupervisorJob()
        return EventTopicData(
            scope = topicScope,
            replay = replay,
            extraBufferCapacity = extraBufferCapacity,
            overflowStrategy = overflowStrategy,
            onError = onError
        )
    }
}
