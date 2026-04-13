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

//@file:OptIn(ExperimentalUuidApi::class)

package com.davils.kore.uuid

public interface Uuid : Comparable<Uuid> {
    
}

//import kotlin.uuid.ExperimentalUuidApi
//import kotlin.uuid.Uuid as KUuid
//
//public class Uuid : Comparable<Uuid> {
//    public val value: String
//    public val version: UuidVersion
//
//    public constructor(value: String, version: UuidVersion = UuidVersion.V7) {
//        this.version = version
//        validate()
//
//        this.value = value
//    }
//
//    public constructor(uuid: Uuid) : this(uuid.value, uuid.version)
//
//    public constructor(version: UuidVersion = UuidVersion.V7) {
//        this.version = version
//        val uuid = when (version) {
//            UuidVersion.V4 -> KUuid.generateV4()
//            UuidVersion.V7 -> KUuid.generateV7()
//        }
//
//        validate()
//        this.value = uuid.toString()
//    }
//
//    override fun compareTo(other: Uuid): Int {
//        TODO("Not yet implemented")
//    }
//
//    private fun validate() {
//        val isValid = when (version) {
//            UuidVersion.V4 -> isValidUuidV4(value)
//            UuidVersion.V7 -> isValidUuidV7(value)
//        }
//        if (!isValid) {
//            throw IllegalArgumentException("Invalid UUID for version: $version -> $value")
//        }
//    }
//
//    public companion object {
//        public const val REGEX_V7_PATTERN: String = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-7[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"
//        public const val REGEX_V4_PATTERN: String = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"
//
//        private val regexV7: Regex = Regex(REGEX_V7_PATTERN)
//        private val regexV4: Regex = Regex(REGEX_V4_PATTERN)
//
//        public fun isValidUuidV7(uuid: String): Boolean = uuid.matches(regexV7)
//        public fun isValidUuidV4(uuid: String): Boolean = uuid.matches(regexV4)
//        public fun random(version: UuidVersion = UuidVersion.V7): Uuid = Uuid(version)
//    }
//}
