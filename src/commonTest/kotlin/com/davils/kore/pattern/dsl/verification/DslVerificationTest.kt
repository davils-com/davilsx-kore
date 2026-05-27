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

package com.davils.kore.pattern.dsl.verification

import com.davils.kore.pattern.creational.dsl.verification.DslVerification
import com.davils.kore.pattern.creational.dsl.verification.DslVerificationData
import com.davils.kore.pattern.creational.dsl.verification.DslVerificationFailure
import com.davils.kore.pattern.creational.dsl.verification.verifyDsl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class DslVerificationTest : FunSpec({

    context("verifyDsl and DslVerificationBuilder") {
        test("creates valid verification when no failures added") {
            val verification = verifyDsl {
                // no failures
            }

            verification.isValid shouldBe true
            verification.failures shouldHaveSize 0
        }

        test("creates invalid verification when single failure added") {
            val verification = verifyDsl {
                fail("Major error")
            }

            verification.isValid shouldBe false
            verification.failures shouldHaveSize 1
            verification.failures[0].message shouldBe "Major error"
            verification.failures[0].field shouldBe null
        }

        test("creates invalid verification when field failure added") {
            val verification = verifyDsl {
                fail("Field error", "someField")
            }

            verification.isValid shouldBe false
            verification.failures shouldHaveSize 1
            verification.failures[0].message shouldBe "Field error"
            verification.failures[0].field shouldBe "someField"
        }

        test("handles empty or blank failure messages and fields") {
            val verification = verifyDsl {
                fail("")
                fail(" ", " ")
            }

            verification.isValid shouldBe false
            verification.failures shouldHaveSize 2
            verification.failures[0].message shouldBe ""
            verification.failures[1].message shouldBe " "
            verification.failures[1].field shouldBe " "
        }

        test("records multiple failures in order") {
            val verification = verifyDsl {
                fail("Error 1")
                fail("Error 2", "field")
                fail("Error 3")
            }

            verification.failures.map { it.message } shouldBe listOf("Error 1", "Error 2", "Error 3")
        }

        test("records failures via failAll with Iterable") {
            val failures = listOf(
                DslVerificationFailure("error 1"),
                DslVerificationFailure("error 2", "field2")
            )

            val verification = verifyDsl {
                failAll(failures)
            }

            verification.failures shouldHaveSize 2
            verification.failures shouldBe failures
        }

        test("records failures via failAll with varargs") {
            val failure1 = DslVerificationFailure("error 1")
            val failure2 = DslVerificationFailure("error 2", "field2")

            val verification = verifyDsl {
                failAll(failure1, failure2)
            }

            verification.failures shouldHaveSize 2
            verification.failures[0] shouldBe failure1
            verification.failures[1] shouldBe failure2
        }

        test("records failures via unaryPlus operator") {
            val verification = verifyDsl {
                +DslVerificationFailure("unary error", "unaryField")
            }

            verification.failures shouldHaveSize 1
            verification.failures[0].message shouldBe "unary error"
            verification.failures[0].field shouldBe "unaryField"
        }

        test("supports complex nested verification logic") {
            val verification = verifyDsl {
                val shouldFail = true
                if (shouldFail) {
                    fail("Nested failure")
                    repeat(2) { i ->
                        fail("Repeat $i")
                    }
                }
            }

            verification.failures.map { it.message } shouldBe listOf("Nested failure", "Repeat 0", "Repeat 1")
        }
    }

    context("DslVerification properties") {
        test("isValid returns false if failures exist") {
            val data = DslVerificationData(listOf(DslVerificationFailure("fail")))
            val verification = DslVerification(data)

            verification.isValid shouldBe false
        }

        test("isValid returns true if failures list is empty") {
            val data = DslVerificationData(emptyList())
            val verification = DslVerification(data)

            verification.isValid shouldBe true
        }

        test("failures property returns the same list as provided in data") {
            val failures = listOf(DslVerificationFailure("fail"))
            val data = DslVerificationData(failures)
            val verification = DslVerification(data)

            verification.failures shouldBe failures
        }
    }
})
