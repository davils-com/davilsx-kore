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

package com.davils.kore.pattern.creational.singleton

import com.davils.kore.pattern.creational.factory.FactoryParameter
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/**
 * A functional interface for retrieving a singleton instance of type [T] based on a parameter of type [P].
 *
 * This represents a parameterized singleton pattern where for each unique parameter (based on equals/hashCode),
 * a single instance is maintained.
 *
 * @param T The type of the singleton instance.
 * @param P The type of the parameter required to retrieve the instance.
 * @since 1.0.0
 */
public fun interface SingletonParameterized<out T, in P : FactoryParameter> {
    /**
     * Retrieves the singleton instance for the given [parameter].
     *
     * @param parameter The parameter used to identify the instance.
     * @return The singleton instance.
     * @since 1.0.0
     */
    public fun getInstance(parameter: P): T

    /**
     * Tries to retrieve the singleton instance for the given [parameter] and returns a [Result].
     *
     * @param parameter The parameter used to identify the instance.
     * @return A [Result] containing the instance or a failure.
     * @since 1.0.0
     */
    public fun getInstanceResult(parameter: P): Result<T> = runCatching { getInstance(parameter) }

    /**
     * Tries to retrieve the singleton instance for the given [parameter] or returns null in case of failure.
     *
     * @param parameter The parameter used to identify the instance.
     * @return The instance or null.
     * @since 1.0.0
     */
    public fun getInstanceOrNull(parameter: P): T? = runCatching { getInstance(parameter) }.getOrNull()
}

/**
 * Implementation of [SingletonParameterized] that caches instances in a map.
 *
 * @param T The type of the singleton instance.
 * @param P The type of the parameter.
 * @param initializer The function to create a new instance for a parameter.
 * @since 1.0.0
 */
internal class SingletonParameterizedImpl<out T, in P : FactoryParameter>(
    private val initializer: (P) -> T
) : SingletonParameterized<T, P>, SynchronizedObject() {

    private val instances = mutableMapOf<P, T>()

    override fun getInstance(parameter: P): T {
        val i = instances[parameter]
        if (i != null) return i

        return synchronized(this) {
            instances.getOrPut(parameter) { initializer(parameter) }
        }
    }
}

/**
 * Creates a [SingletonParameterized] with the given [initializer].
 *
 * @param T The type of the singleton instance.
 * @param P The type of the parameter.
 * @param initializer The function to create a new instance for a parameter.
 * @return A new [SingletonParameterized].
 * @since 1.0.0
 */
public fun <T, P : FactoryParameter> singletonParameterized(
    initializer: (P) -> T
): SingletonParameterized<T, P> = SingletonParameterizedImpl(initializer)
