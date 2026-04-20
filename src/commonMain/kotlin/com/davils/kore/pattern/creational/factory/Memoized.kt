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

package com.davils.kore.pattern.creational.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class FactoryMemoized<out R>(
    private val factory: Factory<R>
) : Factory<R> {
    private val cached: AtomicRef<R?> = atomic(null)

    override fun create(): R {
        return cached.getAndSet(factory.create()) ?: factory.create()
    }
}

// TODO: FactoryParameterizedMemorized -> parameterized version of FactoryMemoized

public class FactoryMemoizedAsync<out R>(
    private val factory: FactoryAsync<R>
) : FactoryAsync<R> {
    private val lock = Mutex()
    // TODO: Return value nullable -> null valid -> use wrapper class for caching
    private var cached: R? = null

    override suspend fun create(): R {
        cached?.let { return it }

        return lock.withLock {
            cached ?: factory.create().also { cached = it }
        }
    }
}

public class FactoryParameterizedMemorizedAsync<P : FactoryParameter, R>(
    private val factory: FactoryParameterizedAsync<P, R>
) : FactoryParameterizedAsync<P, R> {
    private val lock = Mutex()
    private val cached: MutableMap<P, R> = mutableMapOf()

    override suspend fun create(parameter: P): R {
        cached[parameter]?.let { return it }

        return lock.withLock {
            cached[parameter] ?: factory.create(parameter).also { cached[parameter] = it }
        }
    }
}

public fun <R> Factory<R>.memoized(): Factory<R> = FactoryMemoized(this)

public fun <R> FactoryAsync<R>.memoized(): FactoryAsync<R> = FactoryMemoizedAsync(this)

public fun <P : FactoryParameter, R> FactoryParameterizedAsync<P, R>.memoized(): FactoryParameterizedAsync<P, R> = FactoryParameterizedMemorizedAsync(this)