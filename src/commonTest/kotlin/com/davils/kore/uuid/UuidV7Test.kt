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

package com.davils.kore.uuid

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.json.Json

class UuidV7Test : FunSpec({

    context("Construction") {
        test("creates a valid random UUID v7") {
            val uuid = UuidV7()

            UuidV7.isValid(uuid.value).shouldBeTrue()
            uuid.value[14] shouldBe '7'
        }

        test("creates from a valid string") {
            val value = "01890f47-8f3b-7c4e-8b2a-123456789abc"

            val uuid = UuidV7(value)

            uuid.value shouldBe value
            UuidV7.isValid(uuid.value).shouldBeTrue()
        }

        test("copies another instance") {
            val source = UuidV7("01890f47-8f3b-7c4e-8b2a-123456789abc")

            val copy = UuidV7(source)

            copy shouldBe source
            copy.value shouldBe source.value
        }

        test("rejects an invalid value") {
            val exception = shouldThrow<IllegalArgumentException> {
                UuidV7("invalid")
            }

            exception.message shouldContain "Invalid UUID v7"
        }
    }

    context("Validation") {
        test("accepts lowercase valid value") {
            UuidV7.isValid("01890f47-8f3b-7c4e-8b2a-123456789abc").shouldBeTrue()
        }

        test("accepts uppercase valid value") {
            UuidV7.isValid("01890F47-8F3B-7C4E-8B2A-123456789ABC").shouldBeTrue()
        }

        test("rejects version mismatch") {
            UuidV7.isValid("01890f47-8f3b-6c4e-8b2a-123456789abc").shouldBeFalse()
        }

        test("rejects variant mismatch") {
            UuidV7.isValid("01890f47-8f3b-7c4e-cb2a-123456789abc").shouldBeFalse()
        }
    }

    context("Timestamp") {
        test("extracts timestamp from value") {
            val uuid = UuidV7("01890f47-8f3b-7c4e-8b2a-123456789abc")

            uuid.timestamp shouldBe 1688178495291L
            uuid.instant.toEpochMilliseconds() shouldBe 1688178495291L
        }

        test("caches timestamp and instant") {
            val uuid = UuidV7("01890f47-8f3b-7c4e-8b2a-123456789abc")

            uuid.timestamp shouldBe uuid.timestamp
            uuid.instant shouldBe uuid.instant
        }
    }

    context("Extension") {
        test("converts string to UUID v7") {
            val uuid = "01890f47-8f3b-7c4e-8b2a-123456789abc".toUuidV7()

            uuid.value shouldBe "01890f47-8f3b-7c4e-8b2a-123456789abc"
        }

        test("rejects invalid string") {
            shouldThrow<IllegalArgumentException> {
                "invalid".toUuidV7()
            }
        }
    }

    context("Serializer") {
        test("serializes and deserializes value") {
            val original = UuidV7("01890f47-8f3b-7c4e-8b2a-123456789abc")

            val json = Json.encodeToString(UuidV7.serializer(), original)

            json shouldBe "\"01890f47-8f3b-7c4e-8b2a-123456789abc\""

            val decoded = Json.decodeFromString(UuidV7.serializer(), json)

            decoded shouldBe original
        }
    }
})