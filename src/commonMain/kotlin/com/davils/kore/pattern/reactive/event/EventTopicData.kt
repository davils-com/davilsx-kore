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
 * Internal configuration bundle for a single [EventTopic] within an [EventBus].
 *
 * This class carries the coroutine scope and the reactive buffering
 * parameters that drive the topic's underlying `SharedFlow`. Instances are
 * created by the [EventTopicBuilder] and validated before being handed off to
 * the [EventTopic].
 *
 * @since 1.1.1
 */
@ConsistentCopyVisibility
public data class EventTopicData internal constructor(
    /**
     * The [CoroutineScope] in which the topic's dispatch and subscriptions run.
     *
     * The scope is expected to be a child of the bus's owning scope so that
     * disposing the bus cancels every topic scope in a single step.
     *
     * @since 1.1.1
     */
    val scope: CoroutineScope,

    /**
     * The number of events replayed to newly attached subscribers.
     *
     * Must be non-negative. Defaults to 0 in [EventTopicBuilder].
     *
     * @since 1.1.1
     */
    val replay: Int,

    /**
     * The buffer capacity beyond the [replay] count.
     *
     * Must be non-negative. Defaults to 64 in [EventTopicBuilder].
     *
     * @since 1.1.1
     */
    val extraBufferCapacity: Int,

    /**
     * The strategy applied when the topic buffer overflows.
     *
     * Defaults to [BufferOverflow.DROP_OLDEST] in [EventTopicBuilder].
     *
     * @since 1.1.1
     */
    val overflowStrategy: BufferOverflow,

    /**
     * The default error handler for exceptions thrown by subscribers.
     *
     * Invoked when a subscription block throws and does not declare its own
     * error handler.
     *
     * @since 1.1.1
     */
    val onError: suspend (Throwable) -> Unit
) : DslVerifiableData {
    override fun validate(): DslVerification = verifyDsl {
        if (replay < 0) fail("Replay must be non-negative", field = "replay")
        if (extraBufferCapacity < 0) fail("Extra buffer capacity must be non-negative", field = "extraBufferCapacity")
    }
}
