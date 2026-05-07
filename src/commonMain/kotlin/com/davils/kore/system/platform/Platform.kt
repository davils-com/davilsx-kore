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

package com.davils.kore.system.platform

/**
 * Provides information about the current platform and operating system.
 *
 * This object acts as a central point for checking platform-specific traits
 * and identifying the underlying operating system.
 *
 * @since 1.0.0
 */
public object Platform {
    /**
     * The current operating system platform.
     *
     * @since 1.0.0
     */
    public val current: Os
        get() = OsDetector.os

    /**
     * Indicates whether the current platform is a Java Virtual Machine (JVM).
     *
     * @since 1.0.0
     */
    public val isJvm: Boolean
        get() = OsDetector.isJvm

    /**
     * Indicates whether the current operating system is Microsoft Windows.
     *
     * @since 1.0.0
     */
    public val isWindows: Boolean
        get() = current == Os.WINDOWS

    /**
     * Indicates whether the current operating system is Linux.
     *
     * @since 1.0.0
     */
    public val isLinux: Boolean
        get() = current == Os.LINUX

    /**
     * Indicates whether the current operating system is Apple macOS.
     *
     * @since 1.0.0
     */
    public val isMacos: Boolean
        get() = current == Os.MACOS

    /**
     * Indicates whether the current operating system is Apple iOS.
     *
     * @since 1.0.0
     */
    public val isIos: Boolean
        get() = current == Os.IOS

    /**
     * Indicates whether the current operating system is Google Android.
     *
     * @since 1.0.0
     */
    public val isAndroid: Boolean
        get() = current == Os.ANDROID

    /**
     * Indicates whether the current platform is a Web browser (either JS or WASM).
     *
     * @since 1.0.0
     */
    public val isWeb: Boolean
        get() = current == Os.JS || current == Os.WASM

    /**
     * Indicates whether the current operating system is Apple tvOS.
     *
     * @since 1.0.0
     */
    public val isTvOs: Boolean
        get() = current == Os.TVOS

    /**
     * Indicates whether the current operating system is Apple watchOS.
     *
     * @since 1.0.0
     */
    public val isWatchOs: Boolean
        get() = current == Os.WATCHOS

    /**
     * Indicates whether the current operating system is a Unix-based system.
     *
     * This includes Linux, macOS, iOS, tvOS, and watchOS.
     *
     * @since 1.0.0
     */
    public val isUnix: Boolean
        get() = isLinux || isApple

    /**
     * Indicates whether the current operating system is an Apple platform.
     *
     * This includes macOS, iOS, tvOS, and watchOS.
     *
     * @since 1.0.0
     */
    public val isApple: Boolean
        get() = isMacos || isIos || isTvOs || isWatchOs

    /**
     * Indicates whether the current operating system is a mobile platform.
     *
     * This includes iOS and Android.
     *
     * @since 1.0.0
     */
    public val isMobile: Boolean
        get() = isIos || isAndroid
}
