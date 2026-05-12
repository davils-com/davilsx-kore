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
 * Represents a single failure during DSL verification.
 *
 * Each failure consists of a descriptive message and an optional field name
 * that indicates where the verification error occurred.
 *
 * @since 1.0.1
 */
public data class DslVerificationFailure(
    /**
     * A descriptive message explaining the reason for the failure.
     *
     * @since 1.0.1
     */
    public val message: String,

    /**
     * The name of the field that failed verification.
     *
     * This can be `null` if the failure is not specific to a single field.
     *
     * @since 1.0.1
     */
    public val field: String? = null
)
