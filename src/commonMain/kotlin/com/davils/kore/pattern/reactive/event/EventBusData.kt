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

/**
 * Immutable data snapshot describing the full configuration of an [EventBus].
 *
 * Produced by [EventBusBuilder.produce] and consumed by the [eventBus] DSL
 * entry point to construct the actual [EventBus] instance. Keeping the builder
 * output as a pure data type keeps the DSL free of direct implementation
 * dependencies and simplifies testing and inspection.
 *
 * @since 1.1.0
 */
@ConsistentCopyVisibility
public data class EventBusData internal constructor(
    /**
     * The immutable map of declared topics keyed by their unique name.
     *
     * The map is a defensive snapshot taken at the moment the owning
     * [EventBusBuilder] produced this data. Iteration order matches the order
     * in which topics were declared through the DSL.
     *
     * @since 1.1.1
     */
    val topics: Map<String, EventTopic<*>>
)
