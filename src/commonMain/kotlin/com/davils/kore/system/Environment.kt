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

import com.davils.kore.ValueProvider
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
public class EnvironmentScope internal constructor() : ValueProvider<String> {
    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable, or `null` if the variable
     * is not set or environment access is not supported.
     * @since 1.0.0
     */
    override fun getValueOrNull(key: String): String? = Environment.getOrNull(key)
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
