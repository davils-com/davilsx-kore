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
 * Interface for Domain Specific Language (DSL) components that produce a [Result].
 *
 * This interface extends [DslMarker] and is intended for DSL builders where
 * the production process might fail or requires validation, returning a [Result]
 * encapsulating either the successfully produced object or an error.
 *
 * @param T The type of the object produced by this DSL component.
 * @since 1.0.1
 */
@KoreDsl
public interface DslResult<out T> : DslMarker<T> {
    /**
     * Produces and returns the result of the construction process.
     *
     * @return A [Result] containing the produced object of type [T] or a failure.
     * @since 1.0.1
     */
    public fun produce(): Result<T>
}
