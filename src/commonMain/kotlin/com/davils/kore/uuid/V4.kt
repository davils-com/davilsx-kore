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

@file:OptIn(ExperimentalUuidApi::class)

package com.davils.kore.uuid

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid as KUuid

/**
 * Represents a Universally Unique Identifier version 4 (randomly generated).
 *
 * UUID v4 is based on random numbers. This class provides methods to generate,
 * validate, and serialize UUID v4 strings.
 *
 * @since 1.0.0
 */
@Serializable(with = UuidV4.Serializer::class)
public class UuidV4 : Uuid {
    /**
     * The string representation of the UUID v4.
     *
     * @since 1.0.0
     */
    override val value: String

    /**
     * Generates a new random UUID v4.
     *
     * @since 1.0.0
     */
    public constructor() {
        val uuidV4 = KUuid.generateV4()
        value = uuidV4.toString()
        validate()
    }

    /**
     * Creates a UUID v4 from a string representation.
     *
     * @param value The UUID v4 string.
     * @throws IllegalArgumentException If the provided string is not a valid UUID v4.
     * @since 1.0.0
     */
    public constructor(value: String) {
        this.value = value
        validate()
    }

    /**
     * Creates a new UUID v4 as a copy of another [UuidV4] instance.
     *
     * @param uuid The source [UuidV4] instance.
     * @since 1.0.0
     */
    public constructor(uuid: UuidV4) : this(uuid.value)

    /**
     * Validates that the [value] is a valid UUID v4 string.
     *
     * @throws IllegalArgumentException If the value does not match the UUID v4 pattern.
     * @since 1.0.0
     */
    override fun validate() {
        val isValid = isValid(value)
        if (!isValid) {
            throw IllegalArgumentException("Invalid UUID v4: $value")
        }
    }

    /**
     * Companion object for UUID v4 utilities and validation.
     *
     * @since 1.0.0
     */
    public companion object {
        private val regex: Regex = Regex(REGEX_PATTERN)

        /**
         * The regular expression pattern used to validate UUID v4 strings.
         *
         * @since 1.0.0
         */
        public const val REGEX_PATTERN: String = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"

        /**
         * Checks if the given string is a valid UUID v4.
         *
         * @param uuid The string to validate.
         * @return True if the string is a valid UUID v4, false otherwise.
         * @since 1.0.0
         */
        public fun isValid(uuid: String): Boolean = uuid.matches(regex)

        /**
         * Generates a new random UUID v4.
         *
         * @return A new [UuidV4] instance.
         * @since 1.0.0
         */
        public fun random(): UuidV4 = UuidV4()
    }

    /**
     * Serializer for [UuidV4] to be used with kotlinx.serialization.
     *
     * @since 1.0.0
     */
    internal object Serializer : KSerializer<UuidV4> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(encoder: Encoder, value: UuidV4) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): UuidV4 {
            val decodedValue = decoder.decodeString()
            return UuidV4(decodedValue)
        }
    }
}

/**
 * Converts a string to a [UuidV4] instance.
 *
 * @return A new [UuidV4] instance created from this string.
 * @throws IllegalArgumentException If the string is not a valid UUID v4.
 * @since 1.0.0
 */
public fun String.toUuidV4(): UuidV4 = UuidV4(this)
