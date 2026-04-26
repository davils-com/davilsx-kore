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

package com.davils.kore.pattern.creational

/**
 * An interface for objects that can create copies of themselves.
 *
 * This interface represents the Prototype pattern, where new objects are
 * created by copying an existing instance instead of constructing one directly.
 *
 * Implementations define the semantics of the copy operation, such as whether
 * the returned instance is shallow or deep.
 *
 * @param T The concrete prototype type returned by [copy].
 * @since 1.0.0
 */
public interface Prototype<T : Prototype<T>> {
    /**
     * Creates and returns a copy of this instance.
     *
     * The exact behavior of the copy operation depends on the implementation.
     * Implementations may return either a shallow copy or a deep copy.
     *
     * @return A copy of this instance.
     * @since 1.0.0
     */
    public fun copy(): T
}