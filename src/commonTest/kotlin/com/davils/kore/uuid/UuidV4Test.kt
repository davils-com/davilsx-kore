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

class UuidV4Test : FunSpec({

    context("Construction") {
        test("creates a valid random UUID v4") {
            val uuid = UuidV4()

            UuidV4.isValid(uuid.value).shouldBeTrue()
            uuid.value[14] shouldBe '4'
        }

        test("creates from a valid string") {
            val value = "123e4567-e89b-42d3-a456-426614174000"

            val uuid = UuidV4(value)

            uuid.value shouldBe value
            UuidV4.isValid(uuid.value).shouldBeTrue()
        }

        test("copies another instance") {
            val source = UuidV4("123e4567-e89b-42d3-a456-426614174000")

            val copy = UuidV4(source)

            copy shouldBe source
            copy.value shouldBe source.value
        }

        test("rejects an invalid value") {
            val exception = shouldThrow<IllegalArgumentException> {
                UuidV4("invalid")
            }

            exception.message shouldContain "Invalid UUID v4"
        }
    }

    context("Validation") {
        test("accepts lowercase valid value") {
            UuidV4.isValid("123e4567-e89b-42d3-a456-426614174000").shouldBeTrue()
        }

        test("rejects uppercase value") {
            UuidV4.isValid("123E4567-E89B-42D3-A456-426614174000").shouldBeFalse()
        }

        test("rejects version mismatch") {
            UuidV4.isValid("123e4567-e89b-12d3-a456-426614174000").shouldBeFalse()
        }

        test("rejects variant mismatch") {
            UuidV4.isValid("123e4567-e89b-42d3-c456-426614174000").shouldBeFalse()
        }
    }

    context("Extension") {
        test("converts string to UUID v4") {
            val uuid = "123e4567-e89b-42d3-a456-426614174000".toUuidV4()

            uuid.value shouldBe "123e4567-e89b-42d3-a456-426614174000"
        }

        test("rejects invalid string") {
            shouldThrow<IllegalArgumentException> {
                "invalid".toUuidV4()
            }
        }
    }

    context("Serializer") {
        test("serializes and deserializes value") {
            val original = UuidV4("123e4567-e89b-42d3-a456-426614174000")

            val json = Json.encodeToString(UuidV4.serializer(), original)

            json shouldBe "\"123e4567-e89b-42d3-a456-426614174000\""

            val decoded = Json.decodeFromString(UuidV4.serializer(), json)

            decoded shouldBe original
        }
    }
})