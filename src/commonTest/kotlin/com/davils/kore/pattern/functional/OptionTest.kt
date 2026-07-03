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

package com.davils.kore.pattern.functional

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

@Suppress("unused")
class OptionTest : FunSpec({

    test("Some should be some and not none") {
        val some = Option.some(42)
        some.isSome() shouldBe true
        some.isNone() shouldBe false
        some.getOrNull() shouldBe 42
    }

    test("None should be none and not some") {
        val none = Option.none()
        none.isSome() shouldBe false
        none.isNone() shouldBe true
        none.getOrNull() shouldBe null
    }

    test("fromNullable should create Some for non-null and None for null") {
        Option.fromNullable(42) shouldBe Option.Some(42)
        Option.fromNullable(null) shouldBe Option.None
    }

    test("toOption extension should work correctly") {
        42.toOption() shouldBe Option.Some(42)
        val nullInt: Int? = null
        nullInt.toOption() shouldBe Option.None
    }

    test("getOrElse should return value for Some and default for None") {
        Option.some(42).getOrElse { 0 } shouldBe 42
        val none: Option<Int> = Option.none()
        none.getOrElse { 0 } shouldBe 0
    }

    test("getOrThrow should return value for Some and throw for None") {
        Option.some(42).getOrThrow { Exception("Boom") } shouldBe 42
        shouldThrow<Exception> {
            Option.none().getOrThrow { Exception("Boom") }
        }.message shouldBe "Boom"
    }

    test("map should transform Some and keep None as None") {
        Option.some(21).map { it * 2 } shouldBe Option.Some(42)
        Option.none().map { it } shouldBe Option.None
    }

    test("flatMap should transform Some and keep None as None") {
        Option.some(21).flatMap { Option.some(it * 2) } shouldBe Option.Some(42)
        Option.some(21).flatMap { Option.none() } shouldBe Option.None
        Option.none().flatMap { Option.some("test") } shouldBe Option.None
    }

    test("filter should keep Some if predicate matches, otherwise None") {
        Option.some(42).filter { it > 40 } shouldBe Option.Some(42)
        Option.some(42).filter { it < 40 } shouldBe Option.None
        Option.none().filter { true } shouldBe Option.None
    }

    test("fold should apply correct function") {
        Option.some(42).fold({ "none" }, { "some: $it" }) shouldBe "some: 42"
        val none: Option<Int> = Option.none()
        none.fold({ "none" }, { "some: $it" }) shouldBe "none"
    }

    test("onSome and onNone should execute actions") {
        var someExecuted = false
        var noneExecuted = false

        Option.some(42)
            .onSome { someExecuted = true }
            .onNone { noneExecuted = true }

        someExecuted shouldBe true
        noneExecuted shouldBe false

        someExecuted = false
        noneExecuted = false

        Option.none()
            .onSome { someExecuted = true }
            .onNone { noneExecuted = true }

        someExecuted shouldBe false
        noneExecuted shouldBe true
    }

    test("and should return other if Some, else None") {
        Option.some(1).and(Option.some("a")) shouldBe Option.Some("a")
        Option.some(1).and(Option.none()) shouldBe Option.None
        Option.none().and(Option.some("a")) shouldBe Option.None
    }

    test("or should return self if Some, else other") {
        Option.some(1).or(Option.some(2)) shouldBe Option.Some(1)
        val none: Option<Int> = Option.none()
        none.or(Option.some(2)) shouldBe Option.Some(2)
    }

    test("isSomeAnd should work correctly") {
        Option.some(42).isSomeAnd { it > 40 } shouldBe true
        Option.some(42).isSomeAnd { it < 40 } shouldBe false
        Option.none().isSomeAnd { true } shouldBe false
    }

    test("isNoneOr should work correctly") {
        Option.some(42).isNoneOr { it > 40 } shouldBe true
        Option.some(42).isNoneOr { it < 40 } shouldBe false
        Option.none().isNoneOr { false } shouldBe true
    }
})
