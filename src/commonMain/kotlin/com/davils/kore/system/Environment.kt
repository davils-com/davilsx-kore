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

@KoreDsl
public class EnvironmentScope internal constructor() {
    public fun getOrNull(key: String): String? = Environment.getOrNull(key)

    public fun get(key: String): String = getOrNull(key) ?: throw NoSuchElementException("Environment variable '$key' is not set.")

    public fun getOrDefault(key: String, default: String): String = getOrNull(key) ?: default

    public fun getResult(key: String): Result<String> = runCatching { get(key) }

    public fun getOrDefaultResult(key: String, default: String): Result<String> = runCatching { getOrDefault(key, default) }

    public fun <T> withGetOrNull(key: String, block: (String?) -> T?): T? = block(getOrNull(key))

    public fun <T> withGet(key: String, block: (String) -> T): T = block(get(key))

    public fun <T> withGetOrDefault(key: String, default: String, block: (String) -> T): T = block(getOrDefault(key, default))

    public fun <T> withGetResult(key: String, block: (Result<String>) -> T): T = block(getResult(key))

    public fun <T> withGetOrDefaultResult(key: String, default: String, block: (Result<String>) -> T): T = block(getOrDefaultResult(key, default))
}

public fun environment(): EnvironmentScope = EnvironmentScope()

public fun <T> environment(
    ignorePlatformSupport: Boolean = false,
    scope: EnvironmentScope.() -> T
): T = environmentOrNull(ignorePlatformSupport, scope) ?: throw UnsupportedOperationException("Environment variable access is not supported on the current platform (${Platform.current}).")

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
