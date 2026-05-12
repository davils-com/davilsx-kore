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

import com.davils.kore.annotation.KoreDsl
import com.davils.kore.pattern.dsl.verification.DslVerifiableData

/**
 * Base class for DSL components that provide validation for a specific data type.
 *
 * This class defines the core contract for validators within the DSL ecosystem,
 * ensuring that components can provide access to the data they are validating.
 *
 * @param T The type of [DslVerifiableData] that this validator handles.
 * @since 1.0.1
 */
@KoreDsl
public abstract class Validator<out T : DslVerifiableData> {
    /**
     * Returns the data object associated with this validator.
     *
     * This method must be implemented by subclasses to provide the actual data
     * instance that will be validated.
     *
     * @return The data object of type [T].
     * @since 1.0.1
     */
    protected abstract fun data(): T
}
