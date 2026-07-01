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

package com.davils.kore.arguments

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Defines the operational modes of the application.
 *
 * This enum is used to distinguish between different environments such as development
 * and production, allowing for conditional logic based on the current execution context.
 * It is serializable and can be used in configuration or program arguments.
 *
 * @since 1.1.1
 */
@Serializable(with = ApplicationMode.Serializer::class)
public enum class ApplicationMode(
    /**
     * The string representation of the application mode.
     *
     * This value is used for serialization and lookup.
     *
     * @since 1.1.1
     */
    public val value: String
) {
    /**
     * Represents a development environment.
     *
     * Typically used for local testing, debugging, and features that should not be enabled in production.
     *
     * @since 1.1.1
     */
    DEVELOPMENT("development"),

    /**
     * Represents a production environment.
     *
     * The default mode for live applications, emphasizing performance, security, and stability.
     *
     * @since 1.1.1
     */
    PRODUCTION("production");

    public companion object {
        /**
         * Returns the [ApplicationMode] matching the given string value, ignoring case.
         *
         * @param value The string value to look up.
         * @return The matching [ApplicationMode], or null if no match is found.
         * @since 1.1.1
         */
        public fun byValueOrNull(value: String): ApplicationMode? = entries.find {
            it.value.equals(value, ignoreCase = true)
        }

        /**
         * Returns the [ApplicationMode] matching the given string value, ignoring case.
         *
         * @param value The string value to look up.
         * @return The matching [ApplicationMode].
         * @throws IllegalArgumentException If no matching [ApplicationMode] is found.
         * @since 1.1.1
         */
        public fun byValue(value: String): ApplicationMode =
            byValueOrNull(value) ?: throw IllegalArgumentException("No ApplicationMode found for value: $value")
    }

    internal object Serializer : KSerializer<ApplicationMode> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(
            encoder: Encoder,
            value: ApplicationMode
        ) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): ApplicationMode {
            val value = decoder.decodeString()
            return byValue(value)
        }

    }
}
