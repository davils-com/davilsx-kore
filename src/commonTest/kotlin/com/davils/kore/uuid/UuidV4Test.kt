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
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.serialization.json.Json

class UuidV4Test : FunSpec({
    context("Generation") {
        test("should generate a valid UUID v4") {
            val uuid = UuidV4()
            uuid.value shouldMatch Regex(UuidV4.REGEX_PATTERN)
        }

        test("random() should generate a valid UUID v4") {
            val uuid = UuidV4.random()
            uuid.value shouldMatch Regex(UuidV4.REGEX_PATTERN)
        }
    }

    context("Constructors") {
        test("should create instance from valid string") {
            val raw = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
            val uuid = UuidV4(raw)
            uuid.value shouldBe raw
        }

        test("should throw exception for invalid string") {
            val invalid = "not-a-uuid"
            shouldThrow<IllegalArgumentException> {
                UuidV4(invalid)
            }
        }

        test("should throw exception for wrong UUID version (v7)") {
            val v7 = "018f1f51-8b00-7ec1-9491-0d35048740f9"
            shouldThrow<IllegalArgumentException> {
                UuidV4(v7)
            }
        }

        test("should create instance from another UuidV4") {
            val original = UuidV4()
            val copy = UuidV4(original)
            copy.value shouldBe original.value
        }
    }

    context("Extension functions") {
        test("String.toUuidV4() should convert valid string") {
            val raw = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
            val uuid = raw.toUuidV4()
            uuid.value shouldBe raw
        }
    }

    context("Validation") {
        test("isValid should return true for valid v4") {
            UuidV4.isValid("f47ac10b-58cc-4372-a567-0e02b2c3d479") shouldBe true
        }

        test("isValid should return false for invalid v4") {
            UuidV4.isValid("018f1f51-8b00-7ec1-9491-0d35048740f9") shouldBe false
        }
    }

    context("Serialization") {
        test("should serialize and deserialize correctly") {
            val uuid = UuidV4()
            val json = Json.encodeToString(UuidV4.serializer(), uuid)
            val decoded = Json.decodeFromString(UuidV4.serializer(), json)

            decoded shouldBe uuid
            json shouldBe "\"${uuid.value}\""
        }
    }
})