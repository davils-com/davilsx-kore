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

import com.davils.kore.uuid.UuidV7
import kotlin.time.Instant

/**
 * Base class for all events in the system.
 *
 * This class provides a common foundation for event objects, including a unique
 * identifier and a timestamp derived from that identifier. All custom events
 * must extend this class to be used with the [EventBus].
 *
 * @since 1.0.1
 */
public abstract class EventMarker {
    /**
     * The unique identifier for this event.
     *
     * Defaults to a randomly generated UUID v7, which is time-ordered.
     * This property can be overridden if a specific identifier is required.
     *
     * @since 1.0.1
     */
    public open val eventId: UuidV7 = UuidV7.random()

    /**
     * The timestamp indicating when the event was created.
     *
     * This value is derived from the [eventId] and represents the moment
     * the event identifier was generated.
     *
     * @since 1.0.1
     */
    public val instant: Instant = eventId.instant
}
