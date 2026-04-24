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

package com.davils.kore

import kotlinx.atomicfu.locks.SynchronizedObject

/**
 * Provides an interface for interacting with native libraries.
 *
 * This object manages the loading of native libraries and ensures that they are
 * only loaded once. It provides methods to initialize one or more libraries
 * and handle potential exceptions during the loading process.
 *
 * @since 1.0.0
 */
public object NativeInterface : SynchronizedObject() {
    /**
     * A set of names of libraries that have already been successfully loaded.
     *
     * This set is used to prevent redundant attempts to load the same library.
     *
     * @since 1.0.0
     */
    private val loadedLibraries: MutableSet<String> = mutableSetOf()

    /**
     * Initializes a single native library.
     *
     * This is a convenience overload for [initialize] that accepts a single library name.
     *
     * @param library The name of the native library to load.
     * @param onException A callback function invoked if an error occurs during library loading.
     *                    Defaults to throwing a [RuntimeException].
     * @since 1.0.0
     */
    @JvmOverloads
    public fun initialize(
        library: String,
        onException: (Throwable) -> Unit = { throw RuntimeException(it) }
    ) {
        initialize(*arrayOf(library), onException = onException)
    }

    /**
     * Initializes one or more native libraries.
     *
     * This method attempts to load each provided library using [System.loadLibrary].
     * It ensures thread-safe loading and avoids reloading libraries that are already present
     * in [loadedLibraries].
     *
     * @param libraries A variable number of library names to be loaded.
     * @param onException A callback function invoked if an error occurs during the loading
     *                     of any library. Defaults to throwing a [RuntimeException].
     * @since 1.0.0
     */
    @JvmOverloads
    public fun initialize(
        vararg libraries: String,
        onException: (Throwable) -> Unit = { throw RuntimeException(it) }
    ) {
        synchronized(this) {
            libraries.forEach { library ->
                if (library in loadedLibraries) return@forEach

                try {
                    System.loadLibrary(library)
                    loadedLibraries.add(library)
                } catch (e: UnsatisfiedLinkError) {
                    onException(e)
                } catch (e: IllegalCallerException) {
                    onException(e)
                }
            }
        }
    }
}
