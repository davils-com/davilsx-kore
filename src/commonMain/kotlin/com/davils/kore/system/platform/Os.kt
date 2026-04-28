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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents the operating system platform.
 *
 * This enum defines various operating systems supported by the library.
 * It includes desktop, mobile, and web platforms.
 *
 * @since 1.0.0
 */
@Serializable(with = Os.Serializer::class)
public enum class Os(
    /**
     * The string representation of the operating system.
     *
     * This value is used for serialization and identification.
     *
     * @since 1.0.0
     */
    public val value: String
) {
    /**
     * Microsoft Windows operating system.
     *
     * @since 1.0.0
     */
    WINDOWS("windows"),

    /**
     * Linux operating system.
     *
     * @since 1.0.0
     */
    LINUX("linux"),

    /**
     * Apple macOS operating system.
     *
     * @since 1.0.0
     */
    MACOS("macos"),

    /**
     * Apple iOS operating system.
     *
     * @since 1.0.0
     */
    IOS("ios"),

    /**
     * Google Android operating system.
     *
     * @since 1.0.0
     */
    ANDROID("android"),

    /**
     * Web platform using WebAssembly (Wasm).
     *
     * @since 1.0.0
     */
    WASM("webassembly"),

    /**
     * Web platform using JavaScript.
     *
     * @since 1.0.0
     */
    JS("javascript"),

    /**
     * Apple tvOS operating system.
     *
     * @since 1.0.0
     */
    TVOS("tvos"),

    /**
     * Apple watchOS operating system.
     *
     * @since 1.0.0
     */
    WATCHOS("watchos"),

    /**
     * Unknown or unsupported operating system.
     *
     * @since 1.0.0
     */
    UNKNOWN("unknown");

    public companion object {
        /**
         * Returns the [Os] instance matching the given string value, or null if no match is found.
         *
         * The comparison is case-insensitive.
         *
         * @param value The string value to match against [Os.value].
         * @return The matching [Os] instance, or null if not found.
         * @since 1.0.0
         */
        public fun byValueOrNull(value: String): Os? = entries.firstOrNull { it.value.equals(value, ignoreCase = true)}

        /**
         * Returns the [Os] instance matching the given string value, or a default value if no match is found.
         *
         * The comparison is case-insensitive.
         *
         * @param value The string value to match against [Os.value].
         * @param default The fallback [Os] instance to return if no match is found. Defaults to [UNKNOWN].
         * @return The matching [Os] instance, or the provided default.
         * @since 1.0.0
         */
        public fun byValueOrDefault(value: String, default: Os = UNKNOWN): Os = byValueOrNull(value) ?: default

        /**
         * Returns the [Os] instance matching the given string value.
         *
         * The comparison is case-insensitive.
         *
         * @param value The string value to match against [Os.value].
         * @return The matching [Os] instance.
         * @throws IllegalArgumentException If no matching [Os] is found.
         * @since 1.0.0
         */
        public fun byValue(value: String): Os = byValueOrNull(value) ?: throw IllegalArgumentException("No OS found for value: $value")
    }

    internal object Serializer : KSerializer<Os> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(
            encoder: Encoder,
            value: Os
        ) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Os {
            val value = decoder.decodeString()
            return byValue(value)
        }
    }
}