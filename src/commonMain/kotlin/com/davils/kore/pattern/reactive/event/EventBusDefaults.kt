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

import kotlinx.coroutines.channels.BufferOverflow

/**
 * Immutable snapshot of the global topic defaults declared on an [EventBusBuilder].
 *
 * Instances of this class capture the values that every [EventTopicBuilder]
 * uses as its starting configuration. Individual topics may override any of
 * these fields inside their own DSL block without affecting the bus-wide
 * defaults or any sibling topic.
 *
 * @since 1.1.0
 */
internal data class EventBusDefaults(
    /**
     * The default number of events replayed to new subscribers of a topic.
     *
     * Must be non-negative. Defaults to `0`.
     *
     * @since 1.1.0
     */
    val replay: Int = 0,

    /**
     * The default additional buffer capacity beyond [replay] for a topic.
     *
     * Must be non-negative. Defaults to `64`.
     *
     * @since 1.1.0
     */
    val extraBufferCapacity: Int = 64,

    /**
     * The default strategy applied when a topic's buffer is full.
     *
     * Defaults to [BufferOverflow.DROP_OLDEST].
     *
     * @since 1.1.0
     */
    val overflowStrategy: BufferOverflow = BufferOverflow.DROP_OLDEST,

    /**
     * The default error handler for subscription failures on a topic.
     *
     * Invoked when a subscription block throws and does not provide its own
     * error handler. Defaults to a no-op.
     *
     * @since 1.1.0
     */
    val onError: suspend (Throwable) -> Unit = {}
)
