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

package com.davils.kore.arguments

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Provides access to command-line arguments and application configuration parameters.
 *
 * This class parses arguments in the format `key=value` and provides thread-safe access
 * to them. It also includes helper methods for common application flags like `mode`
 * and `dry-run`.
 *
 * The parser is designed to be initialized only once. Subsequent calls to [parse]
 * will be ignored if the instance is already initialized.
 *
 * @since 1.1.1
 */
public open class ProgramArguments {
    private val isInitialized: AtomicBoolean = atomic(false)
    private val arguments: MutableMap<String, String> = mutableMapOf()
    private val mutex: Mutex = Mutex()

    /**
     * Parses the provided arguments and initializes the argument map.
     *
     * Each argument is expected to be in the format `key=value`. Arguments that do not
     * match this format are ignored. This method is thread-safe and ensures that
     * initialization happens only once.
     *
     * @param args An iterable of strings representing the command-line arguments.
     * @since 1.1.1
     */
    public suspend fun parse(args: Iterable<String>) {
        mutex.withLock {
            if (isInitialized.value) {
                return
            }

            args.forEach { arg ->
                if (arg.startsWith("--")) {
                    val parts = arg.split("=", limit = 2)
                    if (parts.size == 2) {
                        arguments[parts[0].removePrefix("--")] = parts[1]
                    }
                }
            }
            isInitialized.compareAndSet(expect = false, update = true)
        }
    }

    /**
     * Parses the provided arguments and initializes the argument map.
     *
     * Convenient overload for [parse] that accepts a variable number of strings.
     *
     * @param args Strings representing the command-line arguments.
     * @since 1.1.1
     */
    public suspend fun parse(vararg args: String) {
        parse(args.asIterable())
    }

    /**
     * Looks up the value associated with the specified key.
     *
     * @param key The argument key to look up.
     * @return The value associated with the key, or null if the key is not found.
     * @since 1.1.1
     */
    public suspend fun lookupOrNull(key: String): String? = mutex.withLock { lookupOrNullUnsafe(key) }

    /**
     * Looks up the value associated with the specified key.
     *
     * @param key The argument key to look up.
     * @return The value associated with the key.
     * @throws IllegalArgumentException If the key is not found in the arguments.
     * @since 1.1.1
     */
    public suspend fun lookup(key: String): String = mutex.withLock { lookupUnsafe(key) }

    /**
     * Checks if the specified key exists in the arguments.
     *
     * @param key The argument key to check.
     * @return True if the key exists, false otherwise.
     * @since 1.1.1
     */
    public suspend fun hasArgument(key: String): Boolean = mutex.withLock { hasArgumentUnsafe(key) }

    /**
     * Returns a copy of all parsed arguments as a map.
     *
     * @return A map containing all keys and their corresponding values.
     * @since 1.1.1
     */
    public suspend fun arguments(): Map<String, String> = mutex.withLock { argumentsUnsafe() }

    /**
     * Returns the value associated with the specified key.
     *
     * This is an operator function allowing for bracket syntax access: `arguments["key"]`.
     *
     * @param key The argument key to look up.
     * @return The value associated with the key.
     * @throws IllegalArgumentException If the key is not found.
     * @since 1.1.1
     */
    public suspend operator fun get(key: String): String = lookup(key)

    /**
     * Determines the current [ApplicationMode] based on the "mode" argument.
     *
     * If the "mode" argument is not provided, it defaults to [ApplicationMode.PRODUCTION].
     *
     * @param key The argument key to look up for the mode. Defaults to "mode".
     *
     * @return The determined [ApplicationMode].
     * @since 1.1.1
     */
    public suspend fun mode(key: String = "mode"): ApplicationMode {
        val mode = lookupOrNull(key) ?: ApplicationMode.PRODUCTION.value
        return ApplicationMode.byValue(mode)
    }

    /**
     * Checks if the application is running in development mode.
     *
     * @param key The argument key to look up for the mode. Defaults to "mode".
     * @return True if [mode] returns [ApplicationMode.DEVELOPMENT], false otherwise.
     * @since 1.1.1
     */
    public suspend fun isDevelopmentMode(key: String = "mode"): Boolean {
        val mode = mode(key)
        return mode == ApplicationMode.DEVELOPMENT
    }

    /**
     * Checks if the application is running in production mode.
     *
     * @param key The argument key to look up for the mode. Defaults to "mode".
     * @return True if [mode] returns [ApplicationMode.PRODUCTION], false otherwise.
     * @since 1.1.1
     */
    public suspend fun isProductionMode(key: String = "mode"): Boolean {
        val mode = mode(key)
        return mode == ApplicationMode.PRODUCTION
    }

    /**
     * Checks if the application is running in dry-run mode.
     *
     * Looks for the "dry-run" argument. If not found, it defaults to false.
     * The value is parsed as a boolean.
     *
     * @param key The argument key to look up for dry-run. Defaults to "dry-run".
     * @param default The default value to return if the argument is not found. Defaults to false.
     *
     * @return True if dry-run is enabled, false otherwise.
     * @since 1.1.1
     */
    public suspend fun isDryRun(
        key: String = "dry-run",
        default: Boolean = false
    ): Boolean {
        val dryRun = lookupOrNull(key) ?: default.toString()
        return dryRun.toBoolean()
    }

    private fun lookupOrNullUnsafe(key: String): String? = arguments[key]

    private fun lookupUnsafe(key: String): String =
        arguments[key] ?: throw IllegalArgumentException("Argument '$key' not found")

    private fun hasArgumentUnsafe(key: String): Boolean = arguments.containsKey(key)

    private fun argumentsUnsafe(): Map<String, String> = arguments.toMap()
}
