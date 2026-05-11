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

package com.davils.kore.system.platform

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import kotlinx.serialization.json.Json

class OsTest : FunSpec({

    test("enum has all expected entries") {
        Os.entries.size shouldBe 10
        Os.entries.map { it.value } shouldBe listOf(
            "windows", "linux", "macos", "ios", "android",
            "webassembly", "javascript", "tvos", "watchos", "unknown"
        )
    }

    test("WINDOWS value is correct") {
        Os.WINDOWS.value shouldBe "windows"
    }

    test("LINUX value is correct") {
        Os.LINUX.value shouldBe "linux"
    }

    test("MACOS value is correct") {
        Os.MACOS.value shouldBe "macos"
    }

    test("IOS value is correct") {
        Os.IOS.value shouldBe "ios"
    }

    test("ANDROID value is correct") {
        Os.ANDROID.value shouldBe "android"
    }

    test("WASM value is correct") {
        Os.WASM.value shouldBe "webassembly"
    }

    test("JS value is correct") {
        Os.JS.value shouldBe "javascript"
    }

    test("TVOS value is correct") {
        Os.TVOS.value shouldBe "tvos"
    }

    test("WATCHOS value is correct") {
        Os.WATCHOS.value shouldBe "watchos"
    }

    test("UNKNOWN value is correct") {
        Os.UNKNOWN.value shouldBe "unknown"
    }

    context("Companion object") {

        context("byValueOrNull") {
            test("returns correct Os for exact match") {
                Os.byValueOrNull("windows") shouldBe Os.WINDOWS
            }

            test("returns correct Os for case-insensitive match") {
                Os.byValueOrNull("WINDOWS") shouldBe Os.WINDOWS
                Os.byValueOrNull("Linux") shouldBe Os.LINUX
            }

            test("returns null for unknown value") {
                Os.byValueOrNull("foo").shouldBeNull()
            }

            test("returns null for empty string") {
                Os.byValueOrNull("").shouldBeNull()
            }

            test("returns null for null - but since param is non-null, not testable directly") {
                // byValueOrNull takes non-null String
            }
        }

        context("byValueOrDefault") {
            test("returns correct Os when match found") {
                Os.byValueOrDefault("android") shouldBe Os.ANDROID
            }

            test("returns default when no match") {
                Os.byValueOrDefault("invalid", Os.LINUX) shouldBe Os.LINUX
            }

            test("uses UNKNOWN as default when not specified") {
                Os.byValueOrDefault("foo") shouldBe Os.UNKNOWN
            }

            test("case-insensitive matching works") {
                Os.byValueOrDefault("MACOS") shouldBe Os.MACOS
            }
        }

        context("byValue") {
            test("returns correct Os for valid input") {
                Os.byValue("javascript") shouldBe Os.JS
            }

            test("case-insensitive matching") {
                Os.byValue("TVOs") shouldBe Os.TVOS
            }

            test("throws exception for invalid input") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Os.byValue("invalid")
                }
                exception.message shouldBe "No OS found for value: invalid"
            }

            test("throws exception for empty string") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Os.byValue("")
                }
                exception.message shouldBe "No OS found for value: "
            }
        }
    }

    context("Serialization") {
        val json = Json { ignoreUnknownKeys = true }

        test("serializes WINDOWS correctly") {
            val jsonString = json.encodeToString(Os.WINDOWS)
            jsonString shouldBe "\"windows\""
        }

        test("deserializes windows to WINDOWS") {
            val os = json.decodeFromString<Os>("\"windows\"")
            os shouldBe Os.WINDOWS
        }

        test("serializes and deserializes roundtrip - WINDOWS") {
            val jsonString = json.encodeToString(Os.WINDOWS)
            val decoded = json.decodeFromString<Os>(jsonString)
            decoded shouldBe Os.WINDOWS
        }

        test("serializes and deserializes roundtrip - ANDROID") {
            val jsonString = json.encodeToString(Os.ANDROID)
            val decoded = json.decodeFromString<Os>(jsonString)
            decoded shouldBe Os.ANDROID
        }

        test("serializes and deserializes roundtrip - UNKNOWN") {
            val jsonString = json.encodeToString(Os.UNKNOWN)
            val decoded = json.decodeFromString<Os>(jsonString)
            decoded shouldBe Os.UNKNOWN
        }

        test("deserializes case variations") {
            json.decodeFromString<Os>("\"WINDOWS\"") shouldBe Os.WINDOWS
            json.decodeFromString<Os>("\"Linux\"") shouldBe Os.LINUX
        }

        test("deserialization throws for invalid value") {
            shouldThrow<IllegalArgumentException> {
                json.decodeFromString<Os>("\"invalid\"")
            }
        }
    }

    context("entries") {
        test("entries contains all enum values") {
            Os.entries shouldBe listOf(
                Os.WINDOWS, Os.LINUX, Os.MACOS, Os.IOS, Os.ANDROID,
                Os.WASM, Os.JS, Os.TVOS, Os.WATCHOS, Os.UNKNOWN
            )
        }
    }
})