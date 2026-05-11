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

package com.davils.kore.event

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.dsl.validation.DslValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.plus

/**
 * Builder for creating and configuring an [EventBus].
 *
 * This class provides a DSL for setting up event bus parameters such as
 * buffer capacity, replay behavior, and error handling.
 *
 * @since 1.0.1
 */
@KoreDsl
public class EventBusBuilder internal constructor(private val scope: CoroutineScope) : DslValidator<EventBusData>() {
    /**
     * The number of events to replay to new subscribers.
     *
     * Must be non-negative. Defaults to 0.
     *
     * @since 1.0.1
     */
    public var replay: Int = 0

    /**
     * The additional buffer capacity beyond the [replay] count.
     *
     * Must be non-negative. Defaults to 64.
     *
     * @since 1.0.1
     */
    public var extraBufferCapacity: Int = 64

    /**
     * The strategy to apply when the event buffer is full.
     *
     * Defaults to [BufferOverflow.DROP_OLDEST].
     *
     * @since 1.0.1
     */
    public var overflowStrategy: BufferOverflow = BufferOverflow.DROP_OLDEST

    /**
     * The default error handler for exceptions thrown during event processing.
     *
     * This handler is invoked when a subscriber throws an exception and does
     * not provide its own error handler.
     *
     * @since 1.0.1
     */
    public var onError: suspend (Throwable) -> Unit = {}

    override fun data(): EventBusData {
        val scope = scope + SupervisorJob()
        return EventBusData(
            scope = scope,
            replay = replay,
            extraBufferCapacity = extraBufferCapacity,
            overflowStrategy = overflowStrategy,
            onError = onError
        )
    }
}
