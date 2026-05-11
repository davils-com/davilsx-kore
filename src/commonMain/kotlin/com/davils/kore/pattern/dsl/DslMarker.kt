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

package com.davils.kore.pattern.dsl

import com.davils.kore.annotation.KoreDsl

/**
 * Base marker interface for all Domain Specific Language (DSL) components.
 *
 * This interface serves as a common root for DSL elements, allowing for
 * type-safe categorization and providing a foundation for DSL structure.
 *
 * @param T The type of the object being built or represented by this DSL component.
 * @since 1.0.1
 */
@KoreDsl
public interface DslMarker<out T>
