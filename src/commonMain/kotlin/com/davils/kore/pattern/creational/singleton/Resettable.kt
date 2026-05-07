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

package com.davils.kore.pattern.creational.singleton

import kotlin.concurrent.Volatile

/**
 * A singleton that can be reset, forcing re-initialization on the next access.
 *
 * This is useful for managing state that needs to be cleared, such as user sessions
 * or caches.
 *
 * @param T The type of the singleton instance.
 * @since 1.0.0
 */
public interface ResettableSingleton<out T> : Singleton<T> {
    /**
     * Resets the singleton instance.
     *
     * The next time [instance] is accessed, it will be re-initialized.
     *
     * @since 1.0.0
     */
    public fun reset()
}

/**
 * Implementation of [ResettableSingleton].
 *
 * @param T The type of the singleton instance.
 * @param initializer The function to initialize the instance.
 * @since 1.0.0
 */
internal class ResettableSingletonImpl<out T>(
    private val initializer: () -> T
) : ResettableSingleton<T>, kotlinx.atomicfu.locks.SynchronizedObject() {

    @Volatile
    private var _instance: Any? = UNINITIALIZED

    override val instance: T
        get() {
            val i = _instance
            if (i !== UNINITIALIZED) {
                @Suppress("UNCHECKED_CAST")
                return i as T
            }

            return kotlinx.atomicfu.locks.synchronized(this) {
                val i2 = _instance
                if (i2 !== UNINITIALIZED) {
                    @Suppress("UNCHECKED_CAST")
                    i2 as T
                } else {
                    val created = initializer()
                    _instance = created
                    created
                }
            }
        }

    override fun reset() {
        kotlinx.atomicfu.locks.synchronized(this) {
            _instance = UNINITIALIZED
        }
    }

    private companion object {
        private val UNINITIALIZED = Any()
    }
}

/**
 * Creates a [ResettableSingleton] with the given [initializer].
 *
 * @param T The type of the singleton instance.
 * @param initializer The function to initialize the instance.
 * @return A new [ResettableSingleton].
 * @since 1.0.0
 */
public fun <T> resettableSingleton(initializer: () -> T): ResettableSingleton<T> =
    ResettableSingletonImpl(initializer)
