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
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid as KUuid

/**
 * Represents a Universally Unique Identifier version 7 (time-ordered).
 *
 * UUID v7 is based on the current timestamp and random bits, ensuring that
 * UUIDs generated later are greater than those generated earlier.
 * This class provides methods to generate, validate, and extract timestamp information.
 *
 * @since 1.0.0
 */
@Serializable(with = UuidV7.Serializer::class)
public class UuidV7 : Uuid {
    /**
     * The string representation of the UUID v7.
     *
     * @since 1.0.0
     */
    override val value: String

    /**
     * The timestamp extracted from the UUID v7 in milliseconds.
     *
     * This value represents the epoch time when the UUID was generated.
     *
     * @since 1.0.0
     */
    public val timestamp: Long by lazy {
        val hex = value.replace("-", "").substring(0, 12)
        hex.toLong(16)
    }

    /**
     * The [Instant] representation of the timestamp extracted from the UUID v7.
     *
     * @since 1.0.0
     */
    public val instant: Instant by lazy {
        Instant.fromEpochMilliseconds(timestamp)
    }

    /**
     * Generates a new time-ordered UUID v7.
     *
     * @since 1.0.0
     */
    public constructor() {
        val uuidV7 = KUuid.generateV7()
        value = uuidV7.toString()
        validate()
    }

    /**
     * Creates a UUID v7 from a string representation.
     *
     * @param value The UUID v7 string.
     * @throws IllegalArgumentException If the provided string is not a valid UUID v7.
     * @since 1.0.0
     */
    public constructor(value: String) {
        this.value = value
        validate()
    }

    /**
     * Creates a new UUID v7 as a copy of another [UuidV7] instance.
     *
     * @param uuid The source [UuidV7] instance.
     * @since 1.0.0
     */
    public constructor(uuid: UuidV7) : this(uuid.value)

    /**
     * Validates that the [value] is a valid UUID v7 string.
     *
     * @throws IllegalArgumentException If the value does not match the UUID v7 pattern.
     * @since 1.0.0
     */
    override fun validate() {
        val isValid = isValid(value)
        if (!isValid) {
            throw IllegalArgumentException("Invalid UUID v7: $value")
        }
    }

    /**
     * Companion object for UUID v7 utilities and validation.
     *
     * @since 1.0.0
     */
    public companion object {
        private val regex: Regex = Regex(REGEX_PATTERN)

        /**
         * The regular expression pattern used to validate UUID v7 strings.
         *
         * @since 1.0.0
         */
        public const val REGEX_PATTERN: String = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-7[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"

        /**
         * Checks if the given string is a valid UUID v7.
         *
         * @param uuid The string to validate.
         * @return True if the string is a valid UUID v7, false otherwise.
         * @since 1.0.0
         */
        public fun isValid(uuid: String): Boolean = uuid.matches(regex)

        /**
         * Generates a new time-ordered UUID v7.
         *
         * @return A new [UuidV7] instance.
         * @since 1.0.0
         */
        public fun random(): UuidV7 = UuidV7()
    }

    /**
     * Serializer for [UuidV7] to be used with kotlinx.serialization.
     *
     * @since 1.0.0
     */
    internal object Serializer : KSerializer<UuidV7> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(encoder: Encoder, value: UuidV7) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): UuidV7 {
            val decodedValue = decoder.decodeString()
            return UuidV7(decodedValue)
        }
    }
}

/**
 * Converts a string to a [UuidV7] instance.
 *
 * @return A new [UuidV7] instance created from this string.
 * @throws IllegalArgumentException If the string is not a valid UUID v7.
 * @since 1.0.0
 */
public fun String.toUuidV7(): UuidV7 = UuidV7(this)
