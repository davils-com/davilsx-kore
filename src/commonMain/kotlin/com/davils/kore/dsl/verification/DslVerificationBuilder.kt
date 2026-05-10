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

package com.davils.kore.dsl.verification

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.dsl.Dsl

@KoreDsl
public class DslVerificationBuilder internal constructor() : Dsl<DslVerificationData> {
    private val failures: MutableList<DslVerificationFailure> = mutableListOf()

    public fun fail(message: String, field: String? = null) {
        failures.add(DslVerificationFailure(message, field))
    }

    public fun fail(failure: DslVerificationFailure) {
        failures.add(failure)
    }

    public fun failAll(failures: Iterable<DslVerificationFailure>) {
        this.failures.addAll(failures)
    }

    public fun failAll(vararg failures: DslVerificationFailure) {
        this.failures.addAll(failures)
    }

    public operator fun DslVerificationFailure.unaryPlus() {
        fail(this)
    }

    override fun produce(): DslVerificationData {
        return DslVerificationData(failures)
    }
}
