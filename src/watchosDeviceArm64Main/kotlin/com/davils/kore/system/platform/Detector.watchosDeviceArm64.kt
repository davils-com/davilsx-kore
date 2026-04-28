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

/**
 * Detector for the operating system running on the Apple Watch (arm64 architecture).
 *
 * @since 1.0.0
 */
internal actual object OsDetector {
    /**
     * Indicates whether the current platform is JVM.
     *
     * @return Always false on Apple Watch.
     * @since 1.0.0
     */
    actual val isJvm: Boolean
        get() = false

    /**
     * The current operating system.
     *
     * @return Always [Os.WATCHOS] on Apple Watch.
     * @since 1.0.0
     */
    actual val os: Os
        get() = Os.WATCHOS
}
