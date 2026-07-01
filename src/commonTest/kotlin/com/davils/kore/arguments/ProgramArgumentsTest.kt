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
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe

class ProgramArgumentsTest : FunSpec({

    context("parse") {

        test("parses key value arguments") {
            val args = ProgramArguments()

            args.parse("--mode=development", "--dry-run=true", "--name=app")

            args.lookup("mode") shouldBe "development"
            args.lookup("dry-run") shouldBe "true"
            args.lookup("name") shouldBe "app"
        }

        test("ignores invalid arguments") {
            val args = ProgramArguments()

            args.parse("--mode=development", "invalid", "also-invalid=")

            args.lookup("mode") shouldBe "development"
            args.lookupOrNull("invalid") shouldBe null
        }

        test("keeps values with equals signs") {
            val args = ProgramArguments()

            args.parse("--token=a=b=c")

            args.lookup("token") shouldBe "a=b=c"
        }

        test("initializes only once") {
            val args = ProgramArguments()

            args.parse("--mode=development", "dry-run=true")
            args.parse("--mode=production", "name=second")

            args.lookup("mode") shouldBe "development"
            args.lookupOrNull("name") shouldBe null
        }
    }

    context("Lookup") {

        test("returns null for missing key") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.lookupOrNull("missing") shouldBe null
        }

        test("returns value for existing key") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.lookup("mode") shouldBe "development"
        }

        test("throws for missing key") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            shouldThrow<IllegalArgumentException> {
                args.lookup("missing")
            }
        }

        test("operator get returns value") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args["mode"] shouldBe "development"
        }

        test("hasArgument returns true for existing key") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.hasArgument("mode").shouldBeTrue()
        }

        test("hasArgument returns false for missing key") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.hasArgument("missing").shouldBeFalse()
        }

        test("arguments returns a copy") {
            val args = ProgramArguments()

            args.parse("--mode=development", "--dry-run=true")

            val snapshot = args.arguments()
            snapshot shouldContain ("mode" to "development")
            snapshot shouldContain ("dry-run" to "true")
        }
    }

    context("Mode") {

        test("defaults to production when missing") {
            val args = ProgramArguments()

            args.parse("--dry-run=true")

            args.mode() shouldBe ApplicationMode.PRODUCTION
        }

        test("returns development mode") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.mode() shouldBe ApplicationMode.DEVELOPMENT
        }

        test("returns production mode") {
            val args = ProgramArguments()

            args.parse("--mode=production")

            args.mode() shouldBe ApplicationMode.PRODUCTION
        }

        test("isDevelopmentMode returns true for development") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.isDevelopmentMode().shouldBeTrue()
        }

        test("isDevelopmentMode returns false for production") {
            val args = ProgramArguments()

            args.parse("--mode=production")

            args.isDevelopmentMode().shouldBeFalse()
        }

        test("isProductionMode returns true for production") {
            val args = ProgramArguments()

            args.parse("--mode=production")

            args.isProductionMode().shouldBeTrue()
        }

        test("isProductionMode returns true when mode is missing") {
            val args = ProgramArguments()

            args.parse("--dry-run=true")

            args.isProductionMode().shouldBeTrue()
        }

        test("mode throws for invalid value") {
            val args = ProgramArguments()

            args.parse("--mode=unknown")

            shouldThrow<IllegalArgumentException> {
                args.mode()
            }
        }
    }

    context("Dry run") {

        test("defaults to false when missing") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.isDryRun().shouldBeFalse()
        }

        test("returns true when value is true") {
            val args = ProgramArguments()

            args.parse("--dry-run=true")

            args.isDryRun().shouldBeTrue()
        }

        test("returns false when value is false") {
            val args = ProgramArguments()

            args.parse("--dry-run=false")

            args.isDryRun().shouldBeFalse()
        }

        test("uses custom key and default value") {
            val args = ProgramArguments()

            args.parse("--custom-flag=true")

            args.isDryRun(key = "custom-flag", default = false).shouldBeTrue()
        }

        test("returns default when custom key is missing") {
            val args = ProgramArguments()

            args.parse("--mode=development")

            args.isDryRun(key = "custom-flag", default = true).shouldBeTrue()
        }
    }
})
