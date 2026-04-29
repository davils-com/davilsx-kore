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

import com.davils.kore.system.PropertiesScope
import com.davils.kore.system.properties
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.withSystemProperty
import io.kotest.extensions.system.withSystemProperties
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf

private const val KEY = "kotest.props.test.key"
private const val KEY_A = "kotest.props.test.a"
private const val KEY_B = "kotest.props.test.b"
private const val KEY_C = "kotest.props.test.c"
private const val KEY_ABSENT = "kotest.props.test.absent"
private const val KEY_COND = "kotest.props.test.conditional"
private const val KEY_UPDATE = "kotest.props.test.update"

class PropertiesScopeTest : FunSpec({

    context("Functions") {

        context("getValueOrNull") {
            test("returns value for existing system property") {
                withSystemProperty(KEY, "hello") {
                    PropertiesScope().getValueOrNull(KEY) shouldBe "hello"
                }
            }

            test("returns null for non-existing system property") {
                PropertiesScope().getValueOrNull(KEY_ABSENT) shouldBe null
            }
        }

        context("setValue") {
            test("returns true and sets system property successfully") {
                withSystemProperty(KEY, null) {
                    val scope = PropertiesScope()
                    val result = scope.setValue(KEY, "value")
                    result shouldBe true
                    System.getProperty(KEY) shouldBe "value"
                }
            }

            test("overwrites an existing system property") {
                withSystemProperty(KEY, "old") {
                    val scope = PropertiesScope()
                    scope.setValue(KEY, "new")
                    System.getProperty(KEY) shouldBe "new"
                }
            }
        }

        context("setValueOrThrow") {
            test("sets the system property without throwing") {
                withSystemProperty(KEY, null) {
                    val scope = PropertiesScope()
                    scope.setValueOrThrow(KEY, "value")
                    System.getProperty(KEY) shouldBe "value"
                }
            }
        }

        context("setValueIfAbsent") {
            test("sets property when it is not yet present") {
                withSystemProperty(KEY, null) {
                    val scope = PropertiesScope()
                    val result = scope.setValueIfAbsent(KEY, "initial")
                    result shouldBe true
                    System.getProperty(KEY) shouldBe "initial"
                }
            }

            test("does not overwrite an already existing property") {
                withSystemProperty(KEY, "existing") {
                    val scope = PropertiesScope()
                    val result = scope.setValueIfAbsent(KEY, "new")
                    result shouldBe false
                    System.getProperty(KEY) shouldBe "existing"
                }
            }
        }

        context("removeValue") {
            test("returns true and removes an existing system property") {
                withSystemProperty(KEY, "value") {
                    val scope = PropertiesScope()
                    val result = scope.removeValue(KEY)
                    result shouldBe true
                    System.getProperty(KEY) shouldBe null
                }
            }

            test("returns true even when removing a non-existing property") {
                PropertiesScope().removeValue(KEY_ABSENT) shouldBe true
            }
        }

        context("removeValueOrThrow") {
            test("removes an existing property without throwing") {
                withSystemProperty(KEY, "value") {
                    val scope = PropertiesScope()
                    scope.removeValueOrThrow(KEY)
                    System.getProperty(KEY) shouldBe null
                }
            }
        }

        context("setAllValues") {
            test("sets all properties from the map") {
                withSystemProperties(mapOf(KEY_A to null, KEY_B to null, KEY_C to null)) {
                    val scope = PropertiesScope()
                    val result = scope.setAllValues(mapOf(KEY_A to "1", KEY_B to "2", KEY_C to "3"))
                    result shouldBe true
                    System.getProperty(KEY_A) shouldBe "1"
                    System.getProperty(KEY_B) shouldBe "2"
                    System.getProperty(KEY_C) shouldBe "3"
                }
            }

            test("returns true for empty map") {
                PropertiesScope().setAllValues(emptyMap()) shouldBe true
            }
        }

        context("setAllValuesOrThrow") {
            test("sets all properties without throwing") {
                withSystemProperties(mapOf(KEY_A to null, KEY_B to null)) {
                    val scope = PropertiesScope()
                    scope.setAllValuesOrThrow(mapOf(KEY_A to "x", KEY_B to "y"))
                    System.getProperty(KEY_A) shouldBe "x"
                    System.getProperty(KEY_B) shouldBe "y"
                }
            }
        }

        context("setValueIf") {
            test("sets property when predicate returns true") {
                withSystemProperty(KEY_COND, null) {
                    val result = PropertiesScope().setValueIf(KEY_COND, "new") { it == null }
                    result shouldBe true
                    System.getProperty(KEY_COND) shouldBe "new"
                }
            }

            test("does not set property when predicate returns false") {
                withSystemProperty(KEY_COND, "existing") {
                    val result = PropertiesScope().setValueIf(KEY_COND, "new") { it == null }
                    result shouldBe false
                    System.getProperty(KEY_COND) shouldBe "existing"
                }
            }

            test("predicate receives current value when key exists") {
                withSystemProperty(KEY_COND, "current") {
                    var received: String? = "not-called"
                    PropertiesScope().setValueIf(KEY_COND, "new") { current ->
                        received = current
                        false
                    }
                    received shouldBe "current"
                }
            }

            test("predicate receives null when key does not exist") {
                var received: String? = "not-called"
                PropertiesScope().setValueIf(KEY_ABSENT, "new") { current ->
                    received = current
                    false
                }
                received shouldBe null
            }
        }

        context("updateValue") {
            test("transforms existing value and sets new one") {
                withSystemProperty(KEY_UPDATE, "hello") {
                    val result = PropertiesScope().updateValue(KEY_UPDATE) { it?.uppercase() ?: "default" }
                    result shouldBe true
                    System.getProperty(KEY_UPDATE) shouldBe "HELLO"
                }
            }

            test("receives null and sets derived value when key is absent") {
                withSystemProperty(KEY_UPDATE, null) {
                    val result = PropertiesScope().updateValue(KEY_UPDATE) { it ?: "default" }
                    result shouldBe true
                    System.getProperty(KEY_UPDATE) shouldBe "default"
                }
            }
        }

        context("setValueResult") {
            test("returns success result when property is set") {
                withSystemProperty(KEY, null) {
                    PropertiesScope().setValueResult(KEY, "value").shouldBeSuccess(Unit)
                }
            }
        }

        context("set operator") {
            test("sets system property via index operator") {
                withSystemProperty(KEY, null) {
                    val scope = PropertiesScope()
                    scope[KEY] = "op-value"
                    System.getProperty(KEY) shouldBe "op-value"
                }
            }

            test("overwrites existing property via index operator") {
                withSystemProperty(KEY, "old") {
                    val scope = PropertiesScope()
                    scope[KEY] = "new"
                    System.getProperty(KEY) shouldBe "new"
                }
            }
        }

        context("get operator (inherited)") {
            test("returns value for existing system property") {
                withSystemProperty(KEY, "val") {
                    PropertiesScope()[KEY] shouldBe "val"
                }
            }

            test("returns null for missing system property") {
                PropertiesScope()[KEY_ABSENT] shouldBe null
            }
        }

        context("contains operator (inherited)") {
            test("returns true when property exists") {
                withSystemProperty(KEY, "present") {
                    (KEY in PropertiesScope()) shouldBe true
                }
            }

            test("returns false when property does not exist") {
                (KEY_ABSENT in PropertiesScope()) shouldBe false
            }
        }

        context("getValueOrThrow (inherited)") {
            test("returns value for existing key") {
                withSystemProperty(KEY, "found") {
                    PropertiesScope().getValueOrThrow(KEY) shouldBe "found"
                }
            }

            test("throws NoSuchElementException for missing key") {
                val exception = runCatching { PropertiesScope().getValueOrThrow(KEY_ABSENT) }.exceptionOrNull()
                exception shouldNotBe null
                exception.shouldBeInstanceOf<NoSuchElementException>()
            }
        }

        context("getValueOrDefault (inherited)") {
            test("returns existing value when key is present") {
                withSystemProperty(KEY, "real") {
                    PropertiesScope().getValueOrDefault(KEY, "fallback") shouldBe "real"
                }
            }

            test("returns default when key is absent") {
                PropertiesScope().getValueOrDefault(KEY_ABSENT, "fallback") shouldBe "fallback"
            }
        }

        context("getValueResult (inherited)") {
            test("returns success for existing property") {
                withSystemProperty(KEY, "val") {
                    PropertiesScope().getValueResult(KEY).shouldBeSuccess("val")
                }
            }

            test("returns failure for missing property") {
                PropertiesScope().getValueResult(KEY_ABSENT).shouldBeFailure<NoSuchElementException>()
            }
        }
    }
})
