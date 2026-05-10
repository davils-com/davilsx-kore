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

package com.davils.kore.dsl.validation

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.dsl.Dsl
import com.davils.kore.dsl.verification.DslVerifiableData
import com.davils.kore.dsl.verification.DslVerificationException

@KoreDsl
public abstract class DslValidator<out D : DslVerifiableData> : Validator<D>(), Dsl<D> {
    override fun produce(): D {
        val data = data()
        return validateData(data)
    }

    private fun validateData(data: D): D {
        val validator = data.validate()
        if (!validator.isValid) {
            throw DslVerificationException(validator.failures)
        }
        return data
    }
}
