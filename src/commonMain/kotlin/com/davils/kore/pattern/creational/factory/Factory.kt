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

package com.davils.kore.pattern.creational.factory

/**
 * A functional interface for creating instances of type [R].
 *
 * This interface represents a standard factory pattern where an object
 * is produced without specifying the exact class of the object that will be created.
 *
 * @param R The type of the object produced by this factory.
 * @since 1.0.0
 */
public fun interface Factory<out R> {
    /**
     * Creates and returns a new instance of type [R].
     *
     * @return A new instance of type [R].
     * @since 1.0.0
     */
    public fun create(): R

    /**
     * Tries to create a new instance of type [R] and returns a [Result].
     *
     * Catches any exceptions that occur during the creation process and
     * encapsulates them in the returned [Result].
     *
     * @return A [Result] containing the new instance of type [R] or a failure.
     * @since 1.0.0
     */
    public fun createResult(): Result<R> = runCatching { create() }

    /**
     * Tries to create a new instance of type [R].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure.
     *
     * @return A new instance of type [R] or null if the creation fails.
     * @since 1.0.0
     */
    public fun createOrNull(): R? {
        val result = runCatching { create() }
        return result.getOrNull()
    }

    /**
     * Tries to create a new instance of type [R] and returns it if it satisfies the [predicate].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure or if the predicate is not satisfied.
     *
     * @param predicate A function to test the created instance.
     * @return A new instance of type [R] that satisfies the predicate, or null.
     * @since 1.0.0
     */
    public fun createIf(predicate: (R) -> Boolean): R? {
        val result = runCatching { create() }
        return result.getOrNull()?.takeIf(predicate)
    }
}

/**
 * A functional interface for asynchronously creating instances of type [R].
 *
 * This interface is the asynchronous counterpart to [Factory], using Coroutines
 * to produce an object.
 *
 * @param R The type of the object produced by this factory.
 * @since 1.0.0
 */
public fun interface FactoryAsync<out R> {
    /**
     * Asynchronously creates and returns a new instance of type [R].
     *
     * @return A new instance of type [R].
     * @since 1.0.0
     */
    public suspend fun create(): R

    /**
     * Asynchronously tries to create a new instance of type [R] and returns a [Result].
     *
     * Catches any exceptions that occur during the creation process and
     * encapsulates them in the returned [Result].
     *
     * @return A [Result] containing the new instance of type [R] or a failure.
     * @since 1.0.0
     */
    public suspend fun createResult(): Result<R> = runCatching { create() }

    /**
     * Asynchronously tries to create a new instance of type [R].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure.
     *
     * @return A new instance of type [R] or null if the creation fails.
     * @since 1.0.0
     */
    public suspend fun createOrNull(): R? {
        val result = runCatching { create() }
        return result.getOrNull()
    }

    /**
     * Asynchronously tries to create a new instance of type [R] and returns it if it satisfies the [predicate].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure or if the predicate is not satisfied.
     *
     * @param predicate A suspend function to test the created instance.
     * @return A new instance of type [R] that satisfies the predicate, or null.
     * @since 1.0.0
     */
    public suspend fun createIf(predicate: suspend (R) -> Boolean): R? {
        val result = runCatching { create() }
        return result.getOrNull()?.takeIf { predicate(it) }
    }
}

public fun <R, T> Factory<R>.map(transform: (R) -> T): Factory<T> = Factory { transform(create()) }

public fun <R, T> FactoryAsync<R>.map(
    transform: (R) -> T
): FactoryAsync<T> = FactoryAsync { transform(create()) }

public fun <R, T> FactoryAsync<R>.mapAsync(
    transform: suspend (R) -> T
): FactoryAsync<T> = FactoryAsync { transform(create()) }