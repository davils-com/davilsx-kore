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
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class DslResultValidatorTest : FunSpec({

    data class TestData(val name: String) : DslVerifiableData {
        override fun validate(): DslVerification = verifyDsl {
            if (name.isBlank()) fail("Name must not be blank")
        }
    }

    class TestResultValidator : DslResultValidator<TestData>() {
        var name: String = ""
        override fun data(): TestData = TestData(name)
    }

    test("DslResultValidator returns success if validation succeeds") {
        val validator = TestResultValidator()
        validator.name = "Valid"

        val result = validator.produce()

        result.shouldBeSuccess {
            it.name shouldBe "Valid"
        }
    }

    test("DslResultValidator returns failure if validation fails") {
        val validator = TestResultValidator()
        validator.name = ""

        val result = validator.produce()

        result.shouldBeFailure {
            it.shouldBeInstanceOf<DslVerificationException>()
            it.failures.size shouldBe 1
            it.failures[0].message shouldBe "Name must not be blank"
        }
    }

    test("DslResultValidator returns failure if data creation throws") {
        class ExplodingValidator : DslResultValidator<TestData>() {
            override fun data(): TestData = throw RuntimeException("Boom")
        }

        val validator = ExplodingValidator()
        val result = validator.produce()

        result.shouldBeFailure {
            it.shouldBeInstanceOf<RuntimeException>()
            it.message shouldBe "Boom"
        }
    }

    test("DslResultValidator can be reused") {
        val validator = TestResultValidator()

        validator.name = "One"
        validator.produce().getOrThrow().name shouldBe "One"

        validator.name = ""
        validator.produce().isFailure shouldBe true

        validator.name = "Two"
        validator.produce().getOrThrow().name shouldBe "Two"
    }

    test("DslResultValidator throws Throwable if it is not an Exception") {
        class ThrowableValidator : DslResultValidator<TestData>() {
            override fun data(): TestData = throw Error("Major Error")
        }

        val validator = ThrowableValidator()
        // Result.failure usually takes Throwable, but the catch is for Exception
        io.kotest.assertions.throwables.shouldThrow<Error> {
            validator.produce()
        }
    }
})
