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

package com.davils.kore.dsl

import com.davils.kore.annotation.KoreDsl

/**
 * Interface for Domain Specific Language (DSL) components that produce a result.
 *
 * This interface extends [DslMarker] and defines the standard contract for DSL
 * builders that can directly instantiate or produce an object of type [T].
 *
 * @param T The type of the object produced by this DSL component.
 * @since 1.0.1
 */
@KoreDsl
public interface Dsl<out T> : DslMarker<T> {
    /**
     * Produces and returns the object constructed by this DSL component.
     *
     * @return The constructed object of type [T].
     * @since 1.0.1
     */
    public fun produce(): T
}
