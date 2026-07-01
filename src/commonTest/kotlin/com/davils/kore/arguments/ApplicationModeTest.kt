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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull

class ApplicationModeTest : FunSpec({
    context("variables") {
        test("DEVELOPMENT has value 'development'") {
            ApplicationMode.DEVELOPMENT.value shouldBe "development"
        }

        test("PRODUCTION has value 'production'") {
            ApplicationMode.PRODUCTION.value shouldBe "production"
        }

        test("enum has exactly two entries") {
            ApplicationMode.entries.size shouldBe 2
        }
    }

    context("companion object") {
        context("byValueOrNull") {
            test("returns DEVELOPMENT for exact match") {
                ApplicationMode.byValueOrNull("development") shouldBe ApplicationMode.DEVELOPMENT
            }

            test("returns PRODUCTION for exact match") {
                ApplicationMode.byValueOrNull("production") shouldBe ApplicationMode.PRODUCTION
            }

            test("returns DEVELOPMENT for uppercase input") {
                ApplicationMode.byValueOrNull("DEVELOPMENT") shouldBe ApplicationMode.DEVELOPMENT
            }

            test("returns PRODUCTION for mixed case input") {
                ApplicationMode.byValueOrNull("Production") shouldBe ApplicationMode.PRODUCTION
            }

            test("returns null for unknown value") {
                ApplicationMode.byValueOrNull("staging").shouldBeNull()
            }

            test("returns null for empty string") {
                ApplicationMode.byValueOrNull("").shouldBeNull()
            }

            test("returns null for blank string") {
                ApplicationMode.byValueOrNull("   ").shouldBeNull()
            }
        }

        context("byValue") {
            test("returns DEVELOPMENT for exact match") {
                ApplicationMode.byValue("development") shouldBe ApplicationMode.DEVELOPMENT
            }

            test("returns PRODUCTION for exact match") {
                ApplicationMode.byValue("production") shouldBe ApplicationMode.PRODUCTION
            }

            test("is case-insensitive for DEVELOPMENT") {
                ApplicationMode.byValue("DEVELOPMENT") shouldBe ApplicationMode.DEVELOPMENT
            }

            test("is case-insensitive for mixed case") {
                ApplicationMode.byValue("Production") shouldBe ApplicationMode.PRODUCTION
            }

            test("throws IllegalArgumentException for unknown value") {
                val exception = shouldThrow<IllegalArgumentException> {
                    ApplicationMode.byValue("staging")
                }
                exception.message shouldBe "No ApplicationMode found for value: staging"
            }

            test("throws IllegalArgumentException for empty string") {
                shouldThrow<IllegalArgumentException> {
                    ApplicationMode.byValue("")
                }
            }

            test("throws IllegalArgumentException for blank string") {
                shouldThrow<IllegalArgumentException> {
                    ApplicationMode.byValue("   ")
                }
            }
        }
    }
})
