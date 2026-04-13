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

package com.davils.kore

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KProperty
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid as KUuid

@Serializable(with = Uuid.Serializer::class)
public class Uuid : Comparable<Uuid> {
    public val value: String

    public val instant: Instant by lazy(LazyThreadSafetyMode.NONE) {
        Instant.fromEpochMilliseconds(epochMilliseconds)
    }

    public val epochMilliseconds: Long by lazy(LazyThreadSafetyMode.NONE) {
        val hi = value.substring(0, 8).toLong(16)
        val lo = value.substring(9, 13).toLong(16)
        (hi shl 16) or lo
    }

    public constructor() {
        val uuid = KUuid.generateV7()
        val raw = uuid.toString()
        validate(raw)
        value = raw
    }

    public constructor(value: String) {
        validate(value)
        this.value = value
    }

    public constructor(uuid: Uuid) : this(uuid.value)

    override fun compareTo(other: Uuid): Int = this.value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is Uuid && this.value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value

    public fun instantLocalDatetime(timezone: TimeZone = TimeZone.UTC): LocalDateTime = instant.toLocalDateTime(timezone)

    public fun toKotlinUuid(): KUuid = KUuid.parse(value)

    public fun toByteArray(): ByteArray = toKotlinUuid().toByteArray()

    private fun validate(value: String) {
        if (!isValidUuidV7(value)) {
            throw IllegalArgumentException("Invalid UUID v7 format")
        }
    }

    public companion object {
        public const val REGEX: String = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-7[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"

        public fun parse(value: String): Uuid = Uuid(value)

        public fun parseOrNull(value: String): Uuid? = try {
            Uuid(value)
        } catch (_: Exception) {
            null
        }

        public fun isValidUuidV7(value: String): Boolean = Regex(REGEX).matches(value)

        public fun random(): Uuid = Uuid()
    }

    internal object Serializer : KSerializer<Uuid> {
        override val descriptor = String.serializer().descriptor
        override fun deserialize(decoder: Decoder): Uuid {
            val value = decoder.decodeString()
            return Uuid(value)
        }

        override fun serialize(encoder: Encoder, value: Uuid) {
            encoder.encodeString(value.value)
        }
    }
}

public fun String.toUuid(): Uuid = Uuid(this)
