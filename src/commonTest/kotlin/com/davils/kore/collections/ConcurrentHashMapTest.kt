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

package com.davils.kore.collections

import com.davils.kore.pattern.functional.Option
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.collections.shouldContain

class ConcurrentHashMapTest : FunSpec({

    test("basic map operations") {
        val map = ConcurrentHashMap<String, Int>()

        map.isEmpty() shouldBe true
        map.size() shouldBe 0L

        map.put("one", 1)
        map.isEmpty() shouldBe false
        map.size() shouldBe 1L
        map.get("one") shouldBe Option.some(1)

        map.put("two", 2)
        map.size() shouldBe 2L
        map.get("two") shouldBe Option.some(2)

        map.containsKey("one") shouldBe true
        map.containsValue(2) shouldBe true
        map.containsKey("three") shouldBe false

        map.remove("one") shouldBe Option.some(1)
        map.size() shouldBe 1L
        map.get("one").isNone() shouldBe true

        map.clear()
        map.isEmpty() shouldBe true
        map.size() shouldBe 0L
    }

    test("collection snapshots") {
        val map = ConcurrentHashMap<String, Int>()
        map.put("a", 1)
        map.put("b", 2)

        val keys = map.keys()
        keys shouldBe setOf("a", "b")

        val values = map.values()
        values.size shouldBe 2
        values shouldContain 1
        values shouldContain 2

        val entries = map.entries()
        entries.size shouldBe 2
        entries.any { it.key == "a" && it.value == 1 } shouldBe true
        entries.any { it.key == "b" && it.value == 2 } shouldBe true
    }

    test("atomic operations") {
        val map = ConcurrentHashMap<String, Int>()

        map.getOrDefault("a", 10) shouldBe 10
        map.put("a", 1)
        map.getOrDefault("a", 10) shouldBe 1

        map.putIfAbsent("a", 2) shouldBe Option.some(1)
        map.get("a") shouldBe Option.some(1)
        map.putIfAbsent("b", 2).isNone() shouldBe true
        map.get("b") shouldBe Option.some(2)

        map.remove("a", 2) shouldBe false
        map.get("a") shouldBe Option.some(1)
        map.remove("a", 1) shouldBe true
        map.get("a").isNone() shouldBe true

        map.put("c", 3)
        map.replace("c", 3, 4) shouldBe true
        map.get("c") shouldBe Option.some(4)
        map.replace("c", 3, 5) shouldBe false
        map.get("c") shouldBe Option.some(4)

        map.replace("c", 5) shouldBe Option.some(4)
        map.get("c") shouldBe Option.some(5)
        map.replace("d", 6).isNone() shouldBe true

        map.putAll(mapOf("e" to 7, "f" to 8))
        map.size() shouldBe 4L
    }

    test("functional operations") {
        val map = ConcurrentHashMap<String, Int>()

        map.computeIfAbsent("a") { 1 } shouldBe 1
        map.get("a") shouldBe Option.some(1)
        map.computeIfAbsent("a") { 2 } shouldBe 1

        map.computeIfPresent("a") { _, v -> Option.some(v + 1) } shouldBe Option.some(2)
        map.get("a") shouldBe Option.some(2)
        map.computeIfPresent("b") { _, _ -> Option.some(10) }.isNone() shouldBe true

        map.compute("c") { _, v -> Option.some(v.fold({ 0 }, { it }) + 3) } shouldBe Option.some(3)
        map.get("c") shouldBe Option.some(3)
        map.compute("c") { _, _ -> Option.none() }.isNone() shouldBe true
        map.containsKey("c") shouldBe false

        map.merge("d", 4) { old, new -> Option.some(old + new) } shouldBe Option.some(4)
        map.merge("d", 5) { old, new -> Option.some(old + new) } shouldBe Option.some(9)
        map.get("d") shouldBe Option.some(9)
        map.merge("d", 1) { _, _ -> Option.none() }.isNone() shouldBe true
        map.containsKey("d") shouldBe false
    }
})
