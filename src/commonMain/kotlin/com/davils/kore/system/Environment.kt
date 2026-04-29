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

@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.davils.kore.system

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.system.platform.Platform

/**
 * Provides access to system environment variables.
 *
 * This object allows retrieving values from the operating system's environment
 * configuration. Support for environment variables may vary across different platforms.
 *
 * @since 1.0.0
 */
public expect object Environment {
    /**
     * Indicates whether the current platform supports environment variable access.
     *
     * @since 1.0.0
     */
    public val isSupported: Boolean

    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable, or `null` if the variable
     * is not set or environment access is not supported.
     * @since 1.0.0
     */
    internal fun getOrNull(key: String): String?
}

/**
 * A scope for accessing environment variables.
 *
 * This class provides a domain-specific language (DSL) for retrieving and
 * transforming environment variables. It can be used directly or within
 * the `environment` builder functions.
 *
 * @since 1.0.0
 */
@KoreDsl
public class EnvironmentScope internal constructor() {
    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable, or `null` if the variable
     * is not set or environment access is not supported.
     * @since 1.0.0
     */
    public fun getOrNull(key: String): String? = Environment.getOrNull(key)

    /**
     * Retrieves the value of the specified environment variable or throws an exception.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable.
     * @throws NoSuchElementException If the environment variable is not set.
     * @since 1.0.0
     */
    public fun getOrThrow(key: String): String = getOrNull(key) ?: throw NoSuchElementException("Environment variable '$key' is not set.")

    /**
     * Retrieves multiple environment variables and returns them as a map.
     *
     * Only variables that are actually set will be included in the resulting map.
     *
     * @param keys An iterable of environment variable names to retrieve.
     * @return A map containing the keys and their corresponding environment variable values.
     * @since 1.0.0
     */
    public fun getAll(keys: Iterable<String>): Map<String, String> {
        val result = mutableMapOf<String, String>()
        for (key in keys) {
            val value = getOrNull(key) ?: continue
            result[key] = value
        }
        return result
    }

    /**
     * Retrieves the value of the specified environment variable or returns a default value.
     *
     * @param key The name of the environment variable to retrieve.
     * @param default The value to return if the environment variable is not set.
     * @return The value of the environment variable, or the provided default value.
     * @since 1.0.0
     */
    public fun getOrDefault(key: String, default: String): String = getOrNull(key) ?: default

    /**
     * Retrieves the value of the specified environment variable wrapped in a [Result].
     *
     * @param key The name of the environment variable to retrieve.
     * @return A [Result] containing the environment variable value, or a failure if not set.
     * @since 1.0.0
     */
    public fun getResult(key: String): Result<String> = runCatching { getOrThrow(key) }

    /**
     * Retrieves the value of the specified environment variable or a default, wrapped in a [Result].
     *
     * @param key The name of the environment variable to retrieve.
     * @param default The default value to use if the environment variable is not set.
     * @return A [Result] containing the environment variable value or the default value.
     * @since 1.0.0
     */
    public fun getOrDefaultResult(key: String, default: String): Result<String> = runCatching { getOrDefault(key, default) }

    /**
     * Executes a block with the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @param block The block to execute with the environment variable value (which may be null).
     * @param T The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <T> withGetOrNull(key: String, block: (String?) -> T?): T? = block(getOrNull(key))

    /**
     * Executes a block with the value of the specified environment variable or throws if not set.
     *
     * @param key The name of the environment variable to retrieve.
     * @param block The block to execute with the environment variable value.
     * @param T The return type of the block.
     * @return The result of the block execution.
     * @throws NoSuchElementException If the environment variable is not set.
     * @since 1.0.0
     */
    public fun <T> withGet(key: String, block: (String) -> T): T = block(getOrThrow(key))

    /**
     * Executes a block with the value of the specified environment variable or a default value.
     *
     * @param key The name of the environment variable to retrieve.
     * @param default The value to use if the environment variable is not set.
     * @param block The block to execute with the retrieved or default value.
     * @param T The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <T> withGetOrDefault(key: String, default: String, block: (String) -> T): T = block(getOrDefault(key, default))

    /**
     * Executes a block with the [Result] of retrieving the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @param block The block to execute with the [Result].
     * @param T The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <T> withGetResult(key: String, block: (Result<String>) -> T): T = block(getResult(key))

    /**
     * Executes a block with the [Result] of retrieving the specified environment variable or a default.
     *
     * @param key The name of the environment variable to retrieve.
     * @param default The default value to use if the environment variable is not set.
     * @param block The block to execute with the [Result].
     * @param T The return type of the block.
     * @return The result of the block execution.
     * @since 1.0.0
     */
    public fun <T> withGetOrDefaultResult(key: String, default: String, block: (Result<String>) -> T): T = block(getOrDefaultResult(key, default))

    /**
     * Transforms the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @param transform The transformation function to apply to the environment variable value.
     * @param T The result type of the transformation.
     * @return The result of the transformation.
     * @since 1.0.0
     */
    public fun <T> mapOrNull(key: String, transform: (String?) -> T?): T? {
        val value = getOrNull(key)
        return transform(value)
    }

    /**
     * Transforms the value of the specified environment variable or throws if not set.
     *
     * @param key The name of the environment variable to retrieve.
     * @param transform The transformation function to apply to the environment variable value.
     * @param T The result type of the transformation.
     * @return The result of the transformation.
     * @throws NoSuchElementException If the environment variable is not set.
     * @since 1.0.0
     */
    public fun <T> map(key: String, transform: (String) -> T): T {
        val value = getOrThrow(key)
        return transform(value)
    }

    /**
     * Checks if the specified environment variable is set.
     *
     * @param key The name of the environment variable to check.
     * @return `true` if the environment variable is set, `false` otherwise.
     * @since 1.0.0
     */
    public operator fun contains(key: String): Boolean = getOrNull(key) != null

    /**
     * Retrieves the value of the specified environment variable using the index operator.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable, or `null` if not set.
     * @since 1.0.0
     */
    public operator fun get(key: String): String? = getOrNull(key)
}

/**
 * Creates a new [EnvironmentScope] instance.
 *
 * @return A new instance of [EnvironmentScope].
 * @since 1.0.0
 */
public fun environment(): EnvironmentScope = EnvironmentScope()

/**
 * Executes the provided block within an [EnvironmentScope].
 *
 * This function ensures that environment variable access is supported on the current platform
 * unless `ignorePlatformSupport` is set to `true`.
 *
 * @param ignorePlatformSupport If `true`, the block will be executed even if the platform
 * does not officially support environment variables.
 * @param scope The block to execute within the [EnvironmentScope].
 * @param T The return type of the scope block.
 * @return The result of the scope block.
 * @throws UnsupportedOperationException If environment access is not supported and
 * `ignorePlatformSupport` is `false`.
 * @since 1.0.0
 */
public fun <T> environment(
    ignorePlatformSupport: Boolean = false,
    scope: EnvironmentScope.() -> T
): T = environmentOrNull(ignorePlatformSupport, scope) ?: throw UnsupportedOperationException("Environment variable access is not supported on the current platform (${Platform.current}).")

/**
 * Executes the provided block within an [EnvironmentScope] or returns `null`.
 *
 * If environment variable access is not supported and `ignorePlatformSupport` is `false`,
 * this function returns `null` instead of throwing an exception.
 *
 * @param ignorePlatformSupport If `true`, the block will be executed even if the platform
 * does not officially support environment variables.
 * @param scope The block to execute within the [EnvironmentScope].
 * @param T The return type of the scope block.
 * @return The result of the scope block, or `null` if environment access is not supported.
 * @since 1.0.0
 */
public fun <T> environmentOrNull(
    ignorePlatformSupport: Boolean = false,
    scope: EnvironmentScope.() -> T
): T? {
    val environmentScope = EnvironmentScope()
    if (!Environment.isSupported && !ignorePlatformSupport) {
        return null
    }
    return environmentScope.scope()
}
