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
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class UuidTest : FunSpec({

    context("Uuid base behavior") {
        test("compares values lexicographically") {
            val smaller = object : Uuid() {
                override val value: String = "a"
                override fun validate() {}
            }
            val larger = object : Uuid() {
                override val value: String = "b"
                override fun validate() {}
            }

            (smaller < larger).shouldBeTrue()
            (larger > smaller).shouldBeTrue()
            (smaller.compareTo(larger) shouldBe -1)
        }

        test("equals uses value equality") {
            val first = object : Uuid() {
                override val value: String = "same"
                override fun validate() {}
            }
            val second = object : Uuid() {
                override val value: String = "same"
                override fun validate() {}
            }
            val different = object : Uuid() {
                override val value: String = "different"
                override fun validate() {}
            }

            first shouldBe second
            first.shouldNotBe(different)
            first.hashCode() shouldBe second.hashCode()
        }

        test("toString returns value") {
            val uuid = object : Uuid() {
                override val value: String = "test-value"
                override fun validate() {}
            }

            uuid.toString() shouldBe "test-value"
        }

        test("property delegation returns value") {
            val uuid = object : Uuid() {
                override val value: String = "delegated-value"
                override fun validate() {}
            }

            val delegated: String by uuid
            delegated shouldBe "delegated-value"
        }
    }

    context("Factory methods") {
        test("randomUuidV4 returns a valid UUID v4") {
            val uuid = Uuid.randomUuidV4()

            UuidV4.isValid(uuid.value).shouldBeTrue()
            uuid.value[14] shouldBe '4'
        }

        test("randomUuidV7 returns a valid UUID v7") {
            val uuid = Uuid.randomUuidV7()

            UuidV7.isValid(uuid.value).shouldBeTrue()
            uuid.value[14] shouldBe '7'
        }
    }
})