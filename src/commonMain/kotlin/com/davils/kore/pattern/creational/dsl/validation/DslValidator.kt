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

package com.davils.kore.pattern.creational.dsl.validation

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.pattern.creational.dsl.Dsl
import com.davils.kore.pattern.creational.dsl.verification.DslVerifiableData
import com.davils.kore.pattern.creational.dsl.verification.DslVerificationException

/**
 * A base class for DSL components that produce validated data.
 *
 * This class combines the functionality of a [Validator] and a [Dsl] component.
 * It ensures that the data produced by the DSL is automatically validated
 * before being returned.
 *
 * @param D The type of [DslVerifiableData] that this validator produces and validates.
 * @since 1.0.1
 */
@KoreDsl
public abstract class DslValidator<out D : DslVerifiableData> : Validator<D>(), Dsl<D> {
    /**
     * Produces the validated data object.
     *
     * This method retrieves the data from the [data] method and performs
     * validation. If the validation fails, a [DslVerificationException] is thrown.
     *
     * @return The validated data object of type [D].
     * @throws DslVerificationException If the data validation fails.
     * @since 1.0.1
     */
    override fun produce(): D {
        val data = data()
        return validateData(data)
    }

    /**
     * Validates the provided data object.
     *
     * This internal method checks the validity of the data using its [DslVerifiableData.validate]
     * method.
     *
     * @param data The data object to validate.
     * @return The validated data object.
     * @throws DslVerificationException If the validation fails.
     * @since 1.0.1
     */
    private fun validateData(data: D): D {
        val validator = data.validate()
        if (!validator.isValid) {
            throw DslVerificationException(validator.failures)
        }
        return data
    }
}
