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

package com.davils.kore.pattern.dsl.validation

import com.davils.kore.pattern.dsl.DslResult
import com.davils.kore.pattern.dsl.verification.DslVerifiableData
import com.davils.kore.pattern.dsl.verification.DslVerificationException

/**
 * A base class for DSL components that produce validated data wrapped in a [Result].
 *
 * This class combines the functionality of a [Validator] and a [DslResult] component.
 * It ensures that the data produced by the DSL is automatically validated,
 * and any errors are captured within a [Result] object.
 *
 * @param D The type of [DslVerifiableData] that this validator produces and validates.
 * @since 1.0.1
 */
public abstract class DslResultValidator<D : DslVerifiableData> : Validator<D>(), DslResult<D> {
    /**
     * Produces the validated data object wrapped in a [Result].
     *
     * This method retrieves the data and performs validation. If validation
     * fails or an exception occurs, a failure [Result] is returned.
     *
     * @return A [Result] containing the validated data object of type [D] or an error.
     * @since 1.0.1
     */
    override fun produce(): Result<D> = try {
        val data = data()
        validateData(data)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Validates the provided data object and returns a [Result].
     *
     * This internal method checks the validity of the data using its [DslVerifiableData.validate]
     * method and returns a success or failure [Result].
     *
     * @param data The data object to validate.
     * @return A [Result] indicating success with the data or failure with an exception.
     * @since 1.0.1
     */
    private fun validateData(data: D): Result<D> {
        val validator = data.validate()
        if (!validator.isValid) {
            return Result.failure(DslVerificationException(validator.failures))
        }
        return Result.success(data)
    }
}
