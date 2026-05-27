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

/**
 * Exception thrown when DSL verification fails.
 *
 * This exception encapsulates a list of [DslVerificationFailure] objects and
 * provides a formatted error message detailing each failure.
 *
 * @since 1.0.1
 */
public class DslVerificationException(
    /**
     * The list of failures that caused this exception.
     *
     * @since 1.0.1
     */
    public val failures: List<DslVerificationFailure>
) : Exception(
    buildString {
        appendLine("DSL verification failed with ${failures.size} failures:")
        failures.forEachIndexed { index, failure ->
            appendLine("  ${index + 1}. ${failure.field ?: "Unknown field"}: ${failure.message}")
        }
    }
)
