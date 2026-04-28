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
import io.kotest.matchers.shouldBe

class DetectorTestJvm : FunSpec({

    test("isJvm should be true on JVM") {
        OsDetector.isJvm shouldBe true
    }

    context("OS Detection Logic") {
        test("should detect Windows") {
            OsDetector.detectOs("Windows 10") shouldBe Os.WINDOWS
            OsDetector.detectOs("WinNT") shouldBe Os.WINDOWS
            OsDetector.detectOs("windows") shouldBe Os.WINDOWS
        }

        test("should detect Linux") {
            OsDetector.detectOs("Linux") shouldBe Os.LINUX
            OsDetector.detectOs("Ubuntu") shouldBe Os.LINUX
            OsDetector.detectOs("CentOS") shouldBe Os.LINUX
            OsDetector.detectOs("Debian") shouldBe Os.LINUX
            OsDetector.detectOs("nux") shouldBe Os.LINUX
        }

        test("should detect macOS") {
            OsDetector.detectOs("Mac OS X") shouldBe Os.MACOS
            OsDetector.detectOs("macOS") shouldBe Os.MACOS
            OsDetector.detectOs("Darwin") shouldBe Os.MACOS
        }

        test("should return UNKNOWN for unsupported systems") {
            OsDetector.detectOs("Solaris") shouldBe Os.UNKNOWN
            OsDetector.detectOs("FreeBSD") shouldBe Os.LINUX
            OsDetector.detectOs("AmigaOS") shouldBe Os.UNKNOWN
        }
    }
})