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

package com.davils.kore

/**
 * A generic provider for retrieving values by key.
 *
 * This interface defines a standard way to access configuration values, environment variables,
 * or any other key-value based data. It provides various methods for safe retrieval,
 * default values, and functional transformations.
 *
 * @param T The type of the values provided.
 * @since 1.0.0
 */
public interface ValueProvider<T> {
    /**
     * Retrieves the value associated with the specified key, or `null` if not found.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or `null` if it does not exist.
     * @since 1.0.0
     */
    public fun getValueOrNull(key: String): T?

    /**
     * Retrieves the value associated with the specified key or throws an exception if not found.
     *
     * @param key The key to look up.
     * @return The value associated with the key.
     * @throws NoSuchElementException If the key is not found.
     * @since 1.0.0
     */
    public fun getValueOrThrow(key: String): T = getValueOrNull(key) ?: throw NoSuchElementException("Environment variable '$key' is not set.")

    /**
     * Retrieves values for multiple keys and returns them as a map.
     *
     * Only keys that have an associated value will be included in the resulting map.
     *
     * @param keys An iterable of keys to retrieve.
     * @return A map containing the keys and their corresponding values.
     * @since 1.0.0
     */
    public fun getAllValues(keys: Iterable<String>): Map<String, T> {
        val result = mutableMapOf<String, T>()
        for (key in keys) {
            val value = getValueOrNull(key) ?: continue
            result[key] = value
        }
        return result
    }

    /**
     * Retrieves the value associated with the key, or returns the [default] value if not found.
     *
     * @param key The key to look up.
     * @param default The value to return if the key is not found.
     * @return The value associated with the key, or the provided default value.
     * @since 1.0.0
     */
    public fun getValueOrDefault(key: String, default: T): T = getValueOrNull(key) ?: default

    /**
     * Retrieves the value associated with the key wrapped in a [Result].
     *
     * @param key The key to look up.
     * @return A [Result] containing the value if found, or a failure if not.
     * @since 1.0.0
     */
    public fun getValueResult(key: String): Result<T> = runCatching { getValueOrThrow(key) }

    /**
     * Retrieves the value associated with the key or a default value, wrapped in a [Result].
     *
     * @param key The key to look up.
     * @param default The default value to use if the key is not found.
     * @return A [Result] containing the value or the default value.
     * @since 1.0.0
     */
    public fun getValueOrDefaultResult(key: String, default: T): Result<T> = runCatching { getValueOrDefault(key, default) }

    /**
     * Executes a block with the value associated with the key (which may be null).
     *
     * @param key The key to look up.
     * @param block The block to execute with the retrieved value.
     * @param R The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <R> useValueOrNull(key: String, block: (T?) -> R?): R? = block(getValueOrNull(key))

    /**
     * Executes a block with the value associated with the key or throws if not found.
     *
     * @param key The key to look up.
     * @param block The block to execute with the retrieved value.
     * @param R The return type of the block.
     * @return The result of the block execution.
     * @throws NoSuchElementException If the key is not found.
     * @since 1.0.0
     */
    public fun <R> useValue(key: String, block: (T) -> R): R = block(getValueOrThrow(key))

    /**
     * Executes a block with the value associated with the key or a default value.
     *
     * @param key The key to look up.
     * @param default The value to use if the key is not found.
     * @param block The block to execute with the value.
     * @param R The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <R> useValueOrDefault(key: String, default: T, block: (T) -> R): R = block(getValueOrDefault(key, default))

    /**
     * Executes a block with the [Result] of retrieving the value for the key.
     *
     * @param key The key to look up.
     * @param block The block to execute with the [Result].
     * @param R The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <R> useValueResult(key: String, block: (Result<T>) -> R): R = block(getValueResult(key))

    /**
     * Executes a block with the [Result] of retrieving the value for the key or a default.
     *
     * @param key The key to look up.
     * @param default The default value to use if the key is not found.
     * @param block The block to execute with the [Result].
     * @param R The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <R> useValueOrDefaultResult(key: String, default: T, block: (Result<T>) -> R): R = block(getValueOrDefaultResult(key, default))

    /**
     * Transforms the value associated with the key using the provided [transform] function.
     *
     * @param key The key to look up.
     * @param transform The transformation function to apply to the value (which may be null).
     * @param R The result type of the transformation.
     * @return The result of the transformation.
     * @since 1.0.0
     */
    public fun <R> mapValueOrNull(key: String, transform: (T?) -> R?): R? {
        val value = getValueOrNull(key)
        return transform(value)
    }

    /**
     * Transforms the value associated with the key or throws if not found.
     *
     * @param key The key to look up.
     * @param transform The transformation function to apply to the retrieved value.
     * @param R The result type of the transformation.
     * @return The result of the transformation.
     * @throws NoSuchElementException If the key is not found.
     * @since 1.0.0
     */
    public fun <R> mapValue(key: String, transform: (T) -> R): R {
        val value = getValueOrThrow(key)
        return transform(value)
    }

    /**
     * Checks if a value exists for the specified key.
     *
     * @param key The key to check.
     * @return `true` if a value exists for the key, `false` otherwise.
     * @since 1.0.0
     */
    public operator fun contains(key: String): Boolean = getValueOrNull(key) != null

    /**
     * Retrieves the value associated with the key using the index operator.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or `null` if not found.
     * @since 1.0.0
     */
    public operator fun get(key: String): T? = getValueOrNull(key)
}
