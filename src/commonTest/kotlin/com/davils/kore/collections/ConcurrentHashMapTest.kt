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
        map.get("one") shouldBe 1

        map.put("two", 2)
        map.size() shouldBe 2L
        map.get("two") shouldBe 2

        map.containsKey("one") shouldBe true
        map.containsValue(2) shouldBe true
        map.containsKey("three") shouldBe false

        map.remove("one") shouldBe 1
        map.size() shouldBe 1L
        map.get("one").shouldBeNull()

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

        map.putIfAbsent("a", 2) shouldBe 1
        map.get("a") shouldBe 1
        map.putIfAbsent("b", 2).shouldBeNull()
        map.get("b") shouldBe 2

        map.remove("a", 2) shouldBe false
        map.get("a") shouldBe 1
        map.remove("a", 1) shouldBe true
        map.get("a").shouldBeNull()

        map.put("c", 3)
        map.replace("c", 3, 4) shouldBe true
        map.get("c") shouldBe 4
        map.replace("c", 3, 5) shouldBe false
        map.get("c") shouldBe 4

        map.replace("c", 5) shouldBe 4
        map.get("c") shouldBe 5
        map.replace("d", 6).shouldBeNull()

        map.putAll(mapOf("e" to 7, "f" to 8))
        map.size() shouldBe 4L
    }

    test("functional operations") {
        val map = ConcurrentHashMap<String, Int>()

        map.computeIfAbsent("a") { 1 } shouldBe 1
        map.get("a") shouldBe 1
        map.computeIfAbsent("a") { 2 } shouldBe 1

        map.computeIfPresent("a") { _, v -> v + 1 } shouldBe 2
        map.get("a") shouldBe 2
        map.computeIfPresent("b") { _,_ -> 10 }.shouldBeNull()

        map.compute("c") { _, v -> (v ?: 0) + 3 } shouldBe 3
        map.get("c") shouldBe 3
        map.compute("c") { _, _ -> null }.shouldBeNull()
        map.containsKey("c") shouldBe false

        map.merge("d", 4) { old, new -> old + new } shouldBe 4
        map.merge("d", 5) { old, new -> old + new } shouldBe 9
        map.get("d") shouldBe 9
        map.merge("d", 1) { _, _ -> null }.shouldBeNull()
        map.containsKey("d") shouldBe false
    }
})
