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

package com.davils.kore.system

import com.davils.kore.ValueProvider
import com.davils.kore.annotation.KoreDsl

/**
 * A scope for managing JVM system properties.
 *
 * This class provides a domain-specific language (DSL) for retrieving, setting,
 * and removing system properties. It implements [ValueProvider] for string values.
 *
 * @since 1.0.0
 */
@KoreDsl
public class PropertiesScope internal constructor() : ValueProvider<String> {
    /**
     * Retrieves the value of the specified system property.
     *
     * @param key The name of the system property.
     * @return The value of the property, or `null` if the property does not exist or an error occurs.
     * @since 1.0.0
     */
    override fun getValueOrNull(key: String): String? = try {
        System.getProperty(key)
    } catch (_: Exception) {
        null
    }

    /**
     * Sets the value of the specified system property.
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @return `true` if the property was successfully set, `false` otherwise.
     * @since 1.0.0
     */
    public fun setValue(key: String, value: String): Boolean {
        return try {
            System.setProperty(key, value)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Sets the value of the specified system property or throws an exception on failure.
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @throws IllegalStateException If setting the property fails.
     * @since 1.0.0
     */
    public fun setValueOrThrow(key: String, value: String) {
        if (!setValue(key, value)) {
            throw IllegalStateException("Failed to set system property '$key' to '$value'.")
        }
    }

    /**
     * Sets the value of the specified system property only if it is not already set.
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @return `true` if the property was set, `false` if it already existed or an error occurred.
     * @since 1.0.0
     */
    public fun setValueIfAbsent(key: String, value: String): Boolean {
        if (getValueOrNull(key) != null) return false
        return setValue(key, value)
    }

    /**
     * Removes the specified system property.
     *
     * @param key The name of the system property to remove.
     * @return `true` if the property was successfully removed, `false` otherwise.
     * @since 1.0.0
     */
    public fun removeValue(key: String): Boolean = try {
        System.clearProperty(key)
        true
    } catch (_: Exception) {
        false
    }

    /**
     * Removes the specified system property or throws an exception on failure.
     *
     * @param key The name of the system property to remove.
     * @throws IllegalStateException If removing the property fails.
     * @since 1.0.0
     */
    public fun removeValueOrThrow(key: String) {
        if (!removeValue(key)) {
            throw IllegalStateException("Failed to remove system property '$key'.")
        }
    }

    /**
     * Sets multiple system properties from the provided map.
     *
     * @param values A map of property keys and values to set.
     * @return `true` if all properties were successfully set, `false` otherwise.
     * @since 1.0.0
     */
    public fun setAllValues(values: Map<String, String>): Boolean {
        return values.all { (key, value) ->
            setValue(key, value)
        }
    }

    /**
     * Sets multiple system properties or throws an exception if any operation fails.
     *
     * @param values A map of property keys and values to set.
     * @throws IllegalStateException If any of the properties fail to be set.
     * @since 1.0.0
     */
    public fun setAllValuesOrThrow(values: Map<String, String>) {
        if (!setAllValues(values)) {
            throw IllegalStateException("Failed to set all system properties.")
        }
    }

    /**
     * Sets a system property if the provided predicate matches the current value.
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @param predicate A function that takes the current value (or null) and returns true if the new value should be set.
     * @return `true` if the property was successfully set, `false` otherwise.
     * @since 1.0.0
     */
    public fun setValueIf(
        key: String,
        value: String,
        predicate: (String?) -> Boolean
    ): Boolean = predicate(getValueOrNull(key)) && setValue(key, value)

    /**
     * Updates a system property by applying a transformation to its current value.
     *
     * @param key The name of the system property.
     * @param transform A function that receives the current value and returns the new value to be set.
     * @return `true` if the property was successfully updated, `false` otherwise.
     * @since 1.0.0
     */
    public fun updateValue(
        key: String,
        transform: (String?) -> String
    ): Boolean {
        val newValue = transform(getValueOrNull(key))
        return setValue(key, newValue)
    }

    /**
     * Sets a system property and returns the result as a [Result].
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @return A [Result] representing success or failure.
     * @since 1.0.0
     */
    public fun setValueResult(key: String, value: String): Result<Unit> = runCatching { setValueOrThrow(key, value) }

    /**
     * Sets the value of the specified system property using the index operator.
     *
     * @param key The name of the system property.
     * @param value The value to set.
     * @throws IllegalStateException If setting the property fails.
     * @since 1.0.0
     */
    public operator fun set(key: String, value: String) {
        setValueOrThrow(key, value)
    }
}

/**
 * Provides a convenient way to access JVM system properties through a [PropertiesScope].
 * This property can be used directly to access system properties without any DSL.
 *
 * @since 1.0.0
 */
public val props: PropertiesScope
    get() = PropertiesScope()

/**
 * Creates a new [PropertiesScope] instance.
 *
 * @return A new instance of [PropertiesScope].
 * @since 1.0.0
 */
public fun properties(): PropertiesScope = PropertiesScope()

/**
 * Executes the provided block within a [PropertiesScope].
 *
 * @param scope The block to execute within the property scope.
 * @param T The return type of the scope block.
 * @return The result of the scope block.
 * @since 1.0.0
 */
public fun <T> properties(scope: PropertiesScope.() -> T): T {
    val scope = PropertiesScope()
    return scope.scope()
}
