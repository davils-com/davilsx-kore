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

import com.davils.kore.dsl.DslResult
import com.davils.kore.dsl.verification.DslVerifiableData
import com.davils.kore.dsl.verification.DslVerificationException

public abstract class DslResultValidator<D : DslVerifiableData> : Validator<D>(), DslResult<D> {
    override fun produce(): Result<D> = try {
        val data = data()
        validateData(data)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun validateData(data: D): Result<D> {
        val validator = data.validate()
        if (!validator.isValid) {
            return Result.failure(DslVerificationException(validator.failures))
        }
        return Result.success(data)
    }
}
