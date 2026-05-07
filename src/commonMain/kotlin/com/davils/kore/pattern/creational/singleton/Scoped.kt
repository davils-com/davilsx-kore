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

/**
 * An implementation of the [Singleton] interface.
 *
 * Uses [lazy] for thread-safe initialization of the instance.
 *
 * @param I The type of the singleton instance.
 * @param initializer A function that creates the singleton instance.
 * @since 1.0.0
 */
public class SingletonScoped<out I> internal constructor(
    initializer: () -> I
) : Singleton<I> {
    override val instance: I by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED, initializer = initializer)
}

/**
 * Creates a scoped [Singleton] instance.
 *
 * The instance is initialized lazily and thread-safely on first access.
 *
 * @param I The type of the singleton instance.
 * @param initializer A function that creates the singleton instance.
 * @return A [Singleton] providing the instance.
 * @since 1.0.0
 */
public fun <I> singletonScoped(initializer: () -> I): Singleton<I> = SingletonScoped(initializer)
