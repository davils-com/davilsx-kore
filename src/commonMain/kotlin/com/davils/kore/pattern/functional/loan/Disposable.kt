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

package com.davils.kore.pattern.functional.loan

/**
 * Represents a resource that can be released or closed.
 *
 * Implementations of this interface should release any held resources, such as memory,
 * file handles, or network connections, when the [dispose] method is called.
 * This is typically used in the context of the Loan Pattern to ensure proper resource management.
 *
 * @since 1.1.0
 */
public interface Disposable {
    /**
     * Releases the resources held by this object.
     *
     * Once disposed, the object should no longer be used. Calling this method multiple
     * times should have no side effects, although this depends on the implementation.
     *
     * @since 1.1.0
     */
    public fun dispose()
}
