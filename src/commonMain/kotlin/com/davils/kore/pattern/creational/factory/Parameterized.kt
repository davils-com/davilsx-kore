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

import kotlinx.coroutines.CancellationException

/**
 * A marker interface for parameters used in parameterized factories.
 *
 * All parameter classes used with [FactoryParameterized] or [FactoryParameterizedAsync]
 * must implement this interface.
 *
 * @since 1.0.0
 */
public interface FactoryParameter

/**
 * A functional interface for creating instances of type [R] using a parameter of type [P].
 *
 * This interface represents a parameterized factory pattern where an object
 * is produced based on a provided configuration or input.
 *
 * @param P The type of the parameter required to create the object.
 * @param R The type of the object produced by this factory.
 * @since 1.0.0
 */
public fun interface FactoryParameterized<P : FactoryParameter, out R> {
    /**
     * Creates and returns a new instance of type [R] using the given [parameter].
     *
     * @param parameter The parameter to use for creation.
     * @return A new instance of type [R].
     * @since 1.0.0
     */
    public fun create(parameter: P): R

    /**
     * Tries to create a new instance of type [R] using the given [parameter] and returns a [Result].
     *
     * Catches any exceptions that occur during the creation process and
     * encapsulates them in the returned [Result].
     *
     * @param parameter The parameter to use for creation.
     * @return A [Result] containing the new instance of type [R] or a failure.
     * @since 1.0.0
     */
    public fun createResult(parameter: P): Result<R> = runCatching { create(parameter) }

    /**
     * Tries to create a new instance of type [R] using the given [parameter].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure.
     *
     * @param parameter The parameter to use for creation.
     * @return A new instance of type [R] or null if the creation fails.
     * @since 1.0.0
     */
    public fun createOrNull(parameter: P): R? {
        val result = runCatching { create(parameter) }
        return result.getOrNull()
    }

    /**
     * Tries to create a new instance of type [R] and returns it if it satisfies the [predicate].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure or if the predicate is not satisfied.
     *
     * @param parameter The parameter to use for creation.
     * @param predicate A function to test the created instance.
     * @return A new instance of type [R] that satisfies the predicate, or null.
     * @since 1.0.0
     */
    public fun createIf(parameter: P, predicate: (P, R) -> Boolean): R? {
        val result = createOrNull(parameter)
        return result?.takeIf { predicate(parameter, it) }
    }
}

/**
 * A functional interface for asynchronously creating instances of type [R] using a parameter of type [P].
 *
 * This interface is the asynchronous counterpart to [FactoryParameterized], using Coroutines
 * to produce an object based on a provided configuration or input.
 *
 * @param P The type of the parameter required to create the object.
 * @param R The type of the object produced by this factory.
 * @since 1.0.0
 */
public fun interface FactoryParameterizedAsync<P : FactoryParameter, out R> {
    /**
     * Asynchronously creates and returns a new instance of type [R] using the given [parameter].
     *
     * @param parameter The parameter to use for creation.
     * @return A new instance of type [R].
     * @since 1.0.0
     */
    public suspend fun create(parameter: P): R

    /**
     * Asynchronously tries to create a new instance of type [R] using the given [parameter] and returns a [Result].
     *
     * Catches any exceptions that occur during the creation process and
     * encapsulates them in the returned [Result].
     *
     * @param parameter The parameter to use for creation.
     * @return A [Result] containing the new instance of type [R] or a failure.
     * @since 1.0.0
     */
    public suspend fun createResult(parameter: P): Result<R> = try {
        Result.success(create(parameter))
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    /**
     * Asynchronously tries to create a new instance of type [R] using the given [parameter].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure.
     *
     * @param parameter The parameter to use for creation.
     * @return A new instance of type [R] or null if the creation fails.
     * @since 1.0.0
     */
    public suspend fun createOrNull(parameter: P): R? = try {
        create(parameter)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    /**
     * Asynchronously tries to create a new instance of type [R] and returns it if it satisfies the [predicate].
     *
     * Catches any exceptions that occur during the creation process and
     * returns null in case of failure or if the predicate is not satisfied.
     *
     * @param parameter The parameter to use for creation.
     * @param predicate A function to test the created instance.
     * @return A new instance of type [R] that satisfies the predicate, or null.
     * @since 1.0.0
     */
    public suspend fun createIf(parameter: P, predicate: (P, R) -> Boolean): R? = try {
        create(parameter).takeIf { predicate(parameter, it) }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }
}

/**
 * Transforms the output of this [FactoryParameterized] using the provided [transform] function.
 *
 * Creates a new parameterized factory that applies the transformation to each instance produced by the original factory.
 *
 * @param P The type of the parameter required to create the object.
 * @param R The type of the object produced by the original factory.
 * @param T The type of the object produced by the new factory.
 * @param transform A function to transform the created instance.
 * @return A new [FactoryParameterized] that produces instances of type [T].
 * @since 1.0.0
 */
public fun <P : FactoryParameter, R, T> FactoryParameterized<P, R>.map(
    transform: (R) -> T
): FactoryParameterized<P, T> = FactoryParameterized { transform(create(it)) }

/**
 * Transforms the output of this [FactoryParameterizedAsync] using the provided [transform] function.
 *
 * Creates a new asynchronous parameterized factory that applies the transformation to each instance produced by the original factory.
 *
 * @param P The type of the parameter required to create the object.
 * @param R The type of the object produced by the original factory.
 * @param T The type of the object produced by the new factory.
 * @param transform A function to transform the created instance.
 * @return A new [FactoryParameterizedAsync] that produces instances of type [T].
 * @since 1.0.0
 */
public fun <P : FactoryParameter, R, T> FactoryParameterizedAsync<P, R>.map(
    transform: (R) -> T
): FactoryParameterizedAsync<P, T> = FactoryParameterizedAsync { transform(create(it)) }

/**
 * Transforms the output of this [FactoryParameterizedAsync] using the provided asynchronous [transform] function.
 *
 * Creates a new asynchronous parameterized factory that applies the asynchronous transformation to each instance produced by the original factory.
 *
 * @param P The type of the parameter required to create the object.
 * @param R The type of the object produced by the original factory.
 * @param T The type of the object produced by the new factory.
 * @param transform A suspend function to transform the created instance.
 * @return A new [FactoryParameterizedAsync] that produces instances of type [T].
 * @since 1.0.0
 */
public fun <P : FactoryParameter, R, T> FactoryParameterizedAsync<P, R>.mapAsync(
    transform: suspend (R) -> T
): FactoryParameterizedAsync<P, T> = FactoryParameterizedAsync { transform(create(it)) }
