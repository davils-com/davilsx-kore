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

package com.davils.kore.system.platform

internal actual object OsDetector {
    /**
     * Keywords used to identify Windows operating systems.
     *
     * @since 1.0.0
     */
    private val WINDOWS_KEYWORDS: Set<String> = setOf("wind", "winnt")

    /**
     * Keywords used to identify Linux or Unix-like operating systems.
     *
     * @since 1.0.0
     */
    private val LINUX_KEYWORDS: Set<String> = setOf("nux", "sun", "bsd", "ubu", "cent", "deb")

    /**
     * Keywords used to identify macOS operating systems.
     *
     * @since 1.0.0
     */
    private val MACOS_KEYWORDS: Set<String> = setOf("mac", "dar")

    /**
     * Indicates whether the current platform is JVM.
     *
     * @since 1.0.0
     */
    actual val isJvm: Boolean
        get() = true

    /**
     * The current operating system detected by the system property "os.name".
     *
     * @since 1.0.0
     */
    actual val os: Os by lazy {
        detectOs(System.getProperty("os.name"))
    }

    /**
     * Detects the operating system based on the provided name string.
     *
     * @param osName The name of the operating system.
     * @return The detected [Os] enum value.
     * @since 1.0.0
     */
    internal fun detectOs(osName: String): Os {
        val name = osName.lowercase()
        return when {
            WINDOWS_KEYWORDS.any { name.contains(it) } -> Os.WINDOWS
            LINUX_KEYWORDS.any { name.contains(it) } -> Os.LINUX
            MACOS_KEYWORDS.any { name.contains(it) } -> Os.MACOS
            else -> Os.UNKNOWN
        }
    }
}
