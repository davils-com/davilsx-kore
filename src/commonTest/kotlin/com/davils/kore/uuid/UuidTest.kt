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

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf

class UuidTest : FunSpec({
    context("Base functionality") {
        test("equality and hashCode") {
            val raw = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
            val uuid1 = UuidV4(raw)
            val uuid2 = UuidV4(raw)
            val uuid3 = UuidV4()

            uuid1 shouldBe uuid2
            uuid1.hashCode() shouldBe uuid2.hashCode()
            uuid1 shouldNotBe uuid3
        }

        test("comparison") {
            val uuid1 = UuidV4("00000000-0000-4000-8000-000000000000")
            val uuid2 = UuidV4("ffffffff-ffff-4fff-bfff-ffffffffffff")

            (uuid1 < uuid2) shouldBe true
            (uuid2 > uuid1) shouldBe true
        }

        test("toString") {
            val uuid = UuidV4()
            uuid.toString() shouldBe uuid.value
        }
    }

    context("Property delegation") {
        test("should delegate correctly") {
            val uuid = UuidV4()
            val delegateValue: String by uuid
            delegateValue shouldBe uuid.value
        }
    }

    context("Companion factory methods") {
        test("randomUuidV4() should return UuidV4 instance") {
            val uuid = Uuid.randomUuidV4()
            uuid.shouldBeInstanceOf<UuidV4>()
        }

        test("randomUuidV7() should return UuidV7 instance") {
            val uuid = Uuid.randomUuidV7()
            uuid.shouldBeInstanceOf<UuidV7>()
        }
    }
})