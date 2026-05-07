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
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Clock

class UuidV7Test : FunSpec({
    context("Generation") {
        test("should generate a valid UUID v7") {
            val uuid = UuidV7()
            uuid.value shouldMatch Regex(UuidV7.REGEX_PATTERN)
        }

        test("should be time-ordered") {
            val uuid1 = UuidV7()
            val uuid2 = UuidV7()

            uuid2 shouldBeGreaterThan uuid1
        }
    }

    context("Timestamp Extraction") {
        test("should extract correct timestamp") {
            val now = Clock.System.now()
            val uuid = UuidV7()

            val extracted = uuid.instant
            val diff: Duration = if (extracted > now) extracted - now else now - extracted
            diff shouldBeLessThan 1.seconds
        }
    }

    context("Constructors") {
        test("should create instance from valid string") {
            val raw = "018f1f51-8b00-7ec1-9491-0d35048740f9"
            val uuid = UuidV7(raw)
            uuid.value shouldBe raw
        }

        test("should throw exception for invalid string") {
            shouldThrow<IllegalArgumentException> {
                UuidV7("not-a-uuid")
            }
        }

        test("should create instance from copy") {
            val original = UuidV7()
            val copy = UuidV7(original)
            copy.value shouldBe original.value
        }
    }

    context("Serialization") {
        test("should serialize and deserialize correctly") {
            val uuid = UuidV7()
            val json = Json.encodeToString(UuidV7.serializer(), uuid)
            val decoded = Json.decodeFromString(UuidV7.serializer(), json)

            decoded shouldBe uuid
            json shouldBe "\"${uuid.value}\""
        }
    }

    context("Extension functions") {
        test("String.toUuidV7() should convert") {
            val raw = "018f1f51-8b00-7ec1-9491-0d35048740f9"
            val uuid = raw.toUuidV7()
            uuid.value shouldBe raw
        }
    }
})