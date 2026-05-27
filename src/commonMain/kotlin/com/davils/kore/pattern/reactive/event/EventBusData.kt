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

import com.davils.kore.pattern.creational.dsl.verification.DslVerifiableData
import com.davils.kore.pattern.creational.dsl.verification.DslVerification
import com.davils.kore.pattern.creational.dsl.verification.verifyDsl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow

/**
 * Internal data configuration for the [EventBus].
 *
 * This class holds the configuration parameters and the coroutine scope
 * used by an [EventBus] instance. It is typically created via [EventBusBuilder].
 *
 * @since 1.0.1
 */
@ConsistentCopyVisibility
public data class EventBusData internal constructor(
    /**
     * The [CoroutineScope] in which the event bus operations run.
     *
     * @since 1.0.1
     */
    val scope: CoroutineScope,

    /**
     * The number of events to be replayed to new subscribers.
     *
     * @since 1.0.1
     */
    val replay: Int,

    /**
     * The additional capacity for the event buffer.
     *
     * @since 1.0.1
     */
    val extraBufferCapacity: Int,

    /**
     * The strategy to use when the event buffer overflows.
     *
     * @since 1.0.1
     */
    val overflowStrategy: BufferOverflow,

    /**
     * The global error handler for event processing exceptions.
     *
     * @since 1.0.1
     */
    val onError: suspend (Throwable) -> Unit
) : DslVerifiableData {
    override fun validate(): DslVerification = verifyDsl {
        if (replay < 0) fail("Replay must be non-negative", field = "replay")
        if (extraBufferCapacity < 0) fail("Extra buffer capacity must be non-negative", field = "extraBufferCapacity")
    }
}

