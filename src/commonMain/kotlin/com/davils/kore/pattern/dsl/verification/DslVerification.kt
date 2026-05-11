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

package com.davils.kore.pattern.dsl.verification

/**
 * A wrapper around [DslVerificationData] that provides high-level verification status.
 *
 * This class allows checking if a DSL configuration is valid and provides
 * access to the list of failures if any occurred.
 *
 * @since 1.0.0
 */
public class DslVerification internal constructor(private val data: DslVerificationData) {
    /**
     * Indicates whether the verification was successful.
     *
     * Returns `true` if there are no failures, `false` otherwise.
     *
     * @since 1.0.0
     */
    public val isValid: Boolean
        get() = data.failures.isEmpty()

    /**
     * The list of failures encountered during verification.
     *
     * @since 1.0.0
     */
    public val failures: List<DslVerificationFailure>
        get() = data.failures
}

/**
 * Creates a new [DslVerification] using a DSL-based builder.
 *
 * This function provides a convenient way to perform multi-step verification
 * and collect failures.
 *
 * @param builder A lambda expression to configure the [DslVerificationBuilder].
 * @return A [DslVerification] instance containing the result of the builder.
 * @since 1.0.0
 */
public fun verifyDsl(builder: DslVerificationBuilder.() -> Unit): DslVerification {
    val builder = DslVerificationBuilder()
    builder.builder()
    val data = builder.produce()
    return DslVerification(data)
}
