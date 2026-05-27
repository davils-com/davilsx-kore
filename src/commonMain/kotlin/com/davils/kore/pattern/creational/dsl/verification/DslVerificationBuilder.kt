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

package com.davils.kore.pattern.creational.dsl.verification

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.pattern.creational.dsl.Dsl

/**
 * A builder class for collecting DSL verification failures.
 *
 * This class implements the [Dsl] interface and provides various methods to
 * record verification failures, either individually or in groups.
 *
 * @since 1.0.1
 */
@KoreDsl
public class DslVerificationBuilder internal constructor() : Dsl<DslVerificationData> {
    private val failures: MutableList<DslVerificationFailure> = mutableListOf()

    /**
     * Records a new verification failure with a message and an optional field.
     *
     * @param message A descriptive message explaining the failure.
     * @param field The name of the field that failed verification (optional).
     * @since 1.0.1
     */
    public fun fail(message: String, field: String? = null) {
        failures.add(DslVerificationFailure(message, field))
    }

    /**
     * Records a specific [DslVerificationFailure].
     *
     * @param failure The failure object to record.
     * @since 1.0.1
     */
    public fun fail(failure: DslVerificationFailure) {
        failures.add(failure)
    }

    /**
     * Records all failures from an [Iterable] collection.
     *
     * @param failures An [Iterable] of failures to record.
     * @since 1.0.1
     */
    public fun failAll(failures: Iterable<DslVerificationFailure>) {
        this.failures.addAll(failures)
    }

    /**
     * Records all provided failures.
     *
     * @param failures One or more failure objects to record.
     * @since 1.0.1
     */
    public fun failAll(vararg failures: DslVerificationFailure) {
        this.failures.addAll(failures)
    }

    /**
     * Extension operator to record a failure using the unary plus operator.
     *
     * This allows adding a failure with the syntax `+DslVerificationFailure(...)`.
     *
     * @receiver The [DslVerificationFailure] to record.
     * @since 1.0.1
     */
    public operator fun DslVerificationFailure.unaryPlus() {
        fail(this)
    }

    /**
     * Produces the [DslVerificationData] containing all recorded failures.
     *
     * @return A [DslVerificationData] instance with the collected failures.
     * @since 1.0.1
     */
    override fun produce(): DslVerificationData {
        return DslVerificationData(failures)
    }
}
