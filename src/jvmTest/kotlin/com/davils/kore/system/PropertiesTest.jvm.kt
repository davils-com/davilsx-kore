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

package com.davils.kore.system

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.withSystemProperty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.result.shouldBeSuccess

class PropertiesTestJvm : FunSpec({

    test("getValueOrNull should return the property value or null") {
        val key = "test.prop.get"
        val value = "test-value"
        withSystemProperty(key, value) {
            properties().getValueOrNull(key) shouldBe value
        }
        properties().getValueOrNull(key) shouldBe null
    }

    test("setValue should set the property value") {
        val key = "test.prop.set"
        val value = "test-value"
        withSystemProperty(key, null) {
            properties().setValue(key, value) shouldBe true
            System.getProperty(key) shouldBe value
        }
    }

    test("setValueOrThrow should set value or throw") {
        val key = "test.prop.set.throw"
        val value = "test-value"
        withSystemProperty(key, null) {
            properties().setValueOrThrow(key, value)
            System.getProperty(key) shouldBe value
        }
    }

    test("setValueIfAbsent should only set if not present") {
        val key = "test.prop.absent"
        val value1 = "value1"
        val value2 = "value2"
        withSystemProperty(key, null) {
            properties().setValueIfAbsent(key, value1) shouldBe true
            System.getProperty(key) shouldBe value1
            properties().setValueIfAbsent(key, value2) shouldBe false
            System.getProperty(key) shouldBe value1
        }
    }

    test("removeValue should remove the property") {
        val key = "test.prop.remove"
        withSystemProperty(key, "value") {
            properties().removeValue(key) shouldBe true
            System.getProperty(key) shouldBe null
        }
    }

    test("removeValueOrThrow should remove or throw") {
        val key = "test.prop.remove.throw"
        withSystemProperty(key, "value") {
            properties().removeValueOrThrow(key)
            System.getProperty(key) shouldBe null
        }
    }

    test("setAllValues should set multiple properties") {
        val key1 = "test.prop.bulk1"
        val key2 = "test.prop.bulk2"
        val values = mapOf(key1 to "v1", key2 to "v2")
        withSystemProperty(key1, null) {
            withSystemProperty(key2, null) {
                properties().setAllValues(values) shouldBe true
                System.getProperty(key1) shouldBe "v1"
                System.getProperty(key2) shouldBe "v2"
            }
        }
    }

    test("setAllValuesOrThrow should set all or throw") {
        val key1 = "test.prop.bulk.throw1"
        val key2 = "test.prop.bulk.throw2"
        val values = mapOf(key1 to "v1", key2 to "v2")
        withSystemProperty(key1, null) {
            withSystemProperty(key2, null) {
                properties().setAllValuesOrThrow(values)
                System.getProperty(key1) shouldBe "v1"
                System.getProperty(key2) shouldBe "v2"
            }
        }
    }

    test("setValueIf should set only if predicate matches") {
        val key = "test.prop.if"
        withSystemProperty(key, null) {
            properties().setValueIf(key, "v1") { it == null } shouldBe true
            System.getProperty(key) shouldBe "v1"
            properties().setValueIf(key, "v2") { it == "wrong" } shouldBe false
            System.getProperty(key) shouldBe "v1"
        }
    }

    test("updateValue should transform existing value") {
        val key = "test.prop.update"
        withSystemProperty(key, "1") {
            properties().updateValue(key) { (it?.toInt() ?: 0).plus(1).toString() } shouldBe true
            System.getProperty(key) shouldBe "2"
        }
    }

    test("updateValue should work with null initial value") {
        val key = "test.prop.update.null"
        withSystemProperty(key, null) {
            properties().updateValue(key) { it ?: "default" } shouldBe true
            System.getProperty(key) shouldBe "default"
        }
    }

    test("setValueResult should return success Result") {
        val key = "test.prop.result"
        val value = "v"
        withSystemProperty(key, null) {
            val result = properties().setValueResult(key, value)
            result.shouldBeSuccess()
            System.getProperty(key) shouldBe value
        }
    }

    test("set operator should set value") {
        val key = "test.prop.operator"
        val value = "v"
        withSystemProperty(key, null) {
            properties()[key] = value
            System.getProperty(key) shouldBe value
        }
    }

    test("properties scope function should work") {
        val key = "test.prop.scope"
        withSystemProperty(key, null) {
            properties {
                setValue(key, "v")
                getValueOrNull(key) shouldBe "v"
            }
            System.getProperty(key) shouldBe "v"
        }
    }
})