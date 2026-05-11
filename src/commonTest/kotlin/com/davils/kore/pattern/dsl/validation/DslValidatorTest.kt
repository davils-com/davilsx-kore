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

package com.davils.kore.pattern.dsl.validation

import com.davils.kore.pattern.dsl.verification.DslVerifiableData
import com.davils.kore.pattern.dsl.verification.DslVerification
import com.davils.kore.pattern.dsl.verification.DslVerificationException
import com.davils.kore.pattern.dsl.verification.verifyDsl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class DslValidatorTest : FunSpec({

    data class TestData(val name: String, val age: Int) : DslVerifiableData {
        override fun validate(): DslVerification = verifyDsl {
            if (name.isBlank()) fail("Name must not be blank", "name")
            if (age < 0) fail("Age must be non-negative", "age")
        }
    }

    class TestDataValidator : DslValidator<TestData>() {
        var name: String = ""
        var age: Int = 0

        override fun data(): TestData = TestData(name, age)
    }

    test("DslValidator produces data if validation succeeds") {
        val validator = TestDataValidator()
        validator.name = "John"
        validator.age = 30

        val result = validator.produce()

        result.name shouldBe "John"
        result.age shouldBe 30
    }

    test("DslValidator throws DslVerificationException if validation fails") {
        val validator = TestDataValidator()
        validator.name = ""
        validator.age = -1

        val exception = shouldThrow<DslVerificationException> {
            validator.produce()
        }

        exception.failures.size shouldBe 2
        exception.failures[0].field shouldBe "name"
        exception.failures[1].field shouldBe "age"
    }

    test("DslValidator can be reused with different values") {
        val validator = TestDataValidator()

        validator.name = "First"
        validator.age = 10
        validator.produce().name shouldBe "First"

        validator.name = "Second"
        validator.age = 20
        validator.produce().name shouldBe "Second"

        validator.name = ""
        shouldThrow<DslVerificationException> {
            validator.produce()
        }
    }

    test("DslValidator preserves failure details in DslVerificationException") {
        val validator = TestDataValidator()
        validator.name = " " // blank
        validator.age = -5

        val exception = shouldThrow<DslVerificationException> {
            validator.produce()
        }

        exception.failures shouldHaveSize 2
        exception.failures[0].message shouldBe "Name must not be blank"
        exception.failures[1].message shouldBe "Age must be non-negative"
    }

    test("DslValidator throws exception from data() if it fails before validation") {
        class BrokenValidator : DslValidator<TestData>() {
            override fun data(): TestData = throw IllegalStateException("State error")
        }

        val validator = BrokenValidator()
        val exception = shouldThrow<IllegalStateException> {
            validator.produce()
        }
        exception.message shouldBe "State error"
    }
})
