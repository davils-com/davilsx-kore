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

package com.davils.kore.system.environment

/**
 * Provides access to system environment variables.
 *
 * @since 1.0.0
 */
public actual object Environment {
    /**
     * Indicates whether the current platform supports environment variable access.
     *
     * @since 1.0.0
     */
    public actual val isSupported: Boolean
        get() = true

    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the environment variable, or `null` if the variable
     * is not set or environment access is not supported.
     * @since 1.0.0
     */
    internal actual fun getOrNull(key: String): String? = try {
        System.getenv(key)
    } catch (_: Exception) {
        null
    }
}