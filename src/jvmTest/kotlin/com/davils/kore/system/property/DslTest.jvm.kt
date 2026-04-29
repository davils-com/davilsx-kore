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

package com.davils.kore.system.property

import com.davils.kore.system.properties
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.withSystemProperty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PropertiesDslTest : FunSpec({

    context("properties() factory") {
        test("creates a valid PropertiesScope instance") {
            properties() shouldNotBe null
        }

        test("returned scope can set and retrieve system properties") {
            withSystemProperty("kotest.props.test.dsl", null) {
                val scope = properties()
                scope.setValue("kotest.props.test.dsl", "dsl-value")
                scope.getValueOrNull("kotest.props.test.dsl") shouldBe "dsl-value"
            }
        }
    }

    context("properties() DSL block") {
        test("executes block and returns its result") {
            withSystemProperty("kotest.props.test.dsl", null) {
                val result = properties {
                    setValue("kotest.props.test.dsl", "block-value")
                    getValueOrNull("kotest.props.test.dsl")
                }
                result shouldBe "block-value"
            }
        }

        test("DSL block has access to PropertiesScope functions") {
            withSystemProperty("kotest.props.test.dsl", null) {
                val result = properties {
                    setValueIfAbsent("kotest.props.test.dsl", "from-dsl")
                    getValueOrNull("kotest.props.test.dsl")
                }
                result shouldBe "from-dsl"
            }
        }

        test("DSL block returns null result correctly") {
            val result = properties {
                getValueOrNull("kotest.props.test.dsl")
            }
            result shouldBe null
        }
    }
})