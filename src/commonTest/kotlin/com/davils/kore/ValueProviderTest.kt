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

package com.davils.kore

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf

private class StringValueProvider(private val data: Map<String, String>) : ValueProvider<String> {
    override fun getValueOrNull(key: String): String? = data[key]
}

private fun providerOf(vararg pairs: Pair<String, String>): ValueProvider<String> =
    StringValueProvider(mapOf(*pairs))

class ValueProviderTest : FunSpec({

    context("Functions") {

        context("getValueOrNull") {
            test("returns value for existing key") {
                val provider = providerOf("key" to "value")
                provider.getValueOrNull("key") shouldBe "value"
            }

            test("returns null for missing key") {
                val provider = providerOf()
                provider.getValueOrNull("missing") shouldBe null
            }

            test("returns null for empty string key not present") {
                val provider = providerOf()
                provider.getValueOrNull("") shouldBe null
            }

            test("returns value for empty string key when present") {
                val provider = providerOf("" to "empty-key-value")
                provider.getValueOrNull("") shouldBe "empty-key-value"
            }
        }

        context("getValueOrThrow") {
            test("returns value for existing key") {
                val provider = providerOf("key" to "value")
                provider.getValueOrThrow("key") shouldBe "value"
            }

            test("throws NoSuchElementException for missing key") {
                val provider = providerOf()
                val exception = runCatching { provider.getValueOrThrow("missing") }.exceptionOrNull()
                exception shouldNotBe null
                exception.shouldBeInstanceOf<NoSuchElementException>()
            }

            test("exception message contains the missing key name") {
                val provider = providerOf()
                val exception = runCatching { provider.getValueOrThrow("MY_KEY") }.exceptionOrNull()
                exception?.message?.contains("MY_KEY") shouldBe true
            }
        }

        context("getAllValues") {
            test("returns map with all found keys") {
                val provider = providerOf("a" to "1", "b" to "2")
                provider.getAllValues(listOf("a", "b")) shouldContainExactly mapOf("a" to "1", "b" to "2")
            }

            test("skips keys with no associated value") {
                val provider = providerOf("a" to "1")
                provider.getAllValues(listOf("a", "missing")) shouldContainExactly mapOf("a" to "1")
            }

            test("returns empty map for empty key list") {
                val provider = providerOf("a" to "1")
                provider.getAllValues(emptyList()).shouldBeEmpty()
            }

            test("returns empty map when no keys match") {
                val provider = providerOf("a" to "1")
                provider.getAllValues(listOf("x", "y")).shouldBeEmpty()
            }

            test("handles duplicate keys in input") {
                val provider = providerOf("a" to "1")
                val result = provider.getAllValues(listOf("a", "a"))
                result shouldContainExactly mapOf("a" to "1")
            }
        }

        context("getValueOrDefault") {
            test("returns existing value when key is present") {
                val provider = providerOf("key" to "value")
                provider.getValueOrDefault("key", "default") shouldBe "value"
            }

            test("returns default value when key is absent") {
                val provider = providerOf()
                provider.getValueOrDefault("missing", "default") shouldBe "default"
            }
        }

        context("getValueResult") {
            test("returns success result for existing key") {
                val provider = providerOf("key" to "value")
                provider.getValueResult("key").shouldBeSuccess("value")
            }

            test("returns failure result for missing key") {
                val provider = providerOf()
                provider.getValueResult("missing").shouldBeFailure()
            }

            test("failure result contains NoSuchElementException") {
                val provider = providerOf()
                val result = provider.getValueResult("missing")
                result.shouldBeFailure<NoSuchElementException>()
            }
        }

        context("getValueOrDefaultResult") {
            test("returns success with existing value when key is present") {
                val provider = providerOf("key" to "value")
                provider.getValueOrDefaultResult("key", "default").shouldBeSuccess("value")
            }

            test("returns success with default value when key is absent") {
                val provider = providerOf()
                provider.getValueOrDefaultResult("missing", "default").shouldBeSuccess("default")
            }
        }

        context("useValueOrNull") {
            test("executes block with existing value") {
                val provider = providerOf("key" to "value")
                val result = provider.useValueOrNull("key") { it?.uppercase() }
                result shouldBe "VALUE"
            }

            test("executes block with null for missing key") {
                val provider = providerOf()
                val result = provider.useValueOrNull("missing") { it }
                result shouldBe null
            }

            test("block can return null explicitly") {
                val provider = providerOf("key" to "value")
                val result = provider.useValueOrNull("key") { null }
                result shouldBe null
            }
        }

        context("useValue") {
            test("executes block with existing value") {
                val provider = providerOf("key" to "hello")
                val result = provider.useValue("key") { it.length }
                result shouldBe 5
            }

            test("throws NoSuchElementException for missing key") {
                val provider = providerOf()
                val exception = runCatching { provider.useValue("missing") { it } }.exceptionOrNull()
                exception.shouldBeInstanceOf<NoSuchElementException>()
            }
        }

        context("useValueOrDefault") {
            test("executes block with existing value") {
                val provider = providerOf("key" to "value")
                val result = provider.useValueOrDefault("key", "default") { it.uppercase() }
                result shouldBe "VALUE"
            }

            test("executes block with default when key is absent") {
                val provider = providerOf()
                val result = provider.useValueOrDefault("missing", "default") { it.uppercase() }
                result shouldBe "DEFAULT"
            }
        }

        context("useValueResult") {
            test("executes block with success result for existing key") {
                val provider = providerOf("key" to "value")
                val result = provider.useValueResult("key") { it.isSuccess }
                result shouldBe true
            }

            test("executes block with failure result for missing key") {
                val provider = providerOf()
                val result = provider.useValueResult("missing") { it.isFailure }
                result shouldBe true
            }
        }

        context("useValueOrDefaultResult") {
            test("executes block with success result containing existing value") {
                val provider = providerOf("key" to "value")
                val result = provider.useValueOrDefaultResult("key", "default") { it.getOrNull() }
                result shouldBe "value"
            }

            test("executes block with success result containing default value") {
                val provider = providerOf()
                val result = provider.useValueOrDefaultResult("missing", "default") { it.getOrNull() }
                result shouldBe "default"
            }
        }

        context("mapValueOrNull") {
            test("transforms existing value") {
                val provider = providerOf("key" to "value")
                val result = provider.mapValueOrNull("key") { it?.length }
                result shouldBe 5
            }

            test("passes null to transform for missing key") {
                val provider = providerOf()
                val result = provider.mapValueOrNull("missing") { it?.length }
                result shouldBe null
            }

            test("transform can return null") {
                val provider = providerOf("key" to "value")
                val result = provider.mapValueOrNull("key") { null }
                result shouldBe null
            }
        }

        context("mapValue") {
            test("transforms existing value") {
                val provider = providerOf("key" to "hello")
                val result = provider.mapValue("key") { it.reversed() }
                result shouldBe "olleh"
            }

            test("throws NoSuchElementException for missing key") {
                val provider = providerOf()
                val exception = runCatching { provider.mapValue("missing") { it } }.exceptionOrNull()
                exception.shouldBeInstanceOf<NoSuchElementException>()
            }
        }

        context("contains operator") {
            test("returns true when key exists") {
                val provider = providerOf("key" to "value")
                ("key" in provider) shouldBe true
            }

            test("returns false when key does not exist") {
                val provider = providerOf()
                ("missing" in provider) shouldBe false
            }

            test("returns false for empty provider") {
                val provider = providerOf()
                ("any" in provider) shouldBe false
            }
        }

        context("get operator") {
            test("returns value for existing key") {
                val provider = providerOf("key" to "value")
                provider["key"] shouldBe "value"
            }

            test("returns null for missing key") {
                val provider = providerOf()
                provider["missing"] shouldBe null
            }
        }
    }
})