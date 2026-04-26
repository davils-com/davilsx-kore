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

package com.davils.kore.uuid

import kotlin.reflect.KProperty

/**
 * Base class for Universally Unique Identifiers (UUID).
 *
 * This class provides common functionality for different UUID versions,
 * including comparison, equality, and property delegation.
 *
 * @since 1.0.0
 */
public abstract class Uuid : Comparable<Uuid> {
    /**
     * The string representation of the UUID.
     *
     * @since 1.0.0
     */
    public abstract val value: String

    /**
     * Validates the format of the UUID value.
     *
     * This method is called during construction to ensure the value conforms
     * to the specific UUID version's constraints.
     *
     * @throws IllegalArgumentException If the value is not a valid UUID of the expected version.
     * @since 1.0.0
     */
    protected abstract fun validate()

    /**
     * Compares this UUID with the specified UUID for order.
     *
     * @param other The UUID to be compared.
     * @return A negative integer, zero, or a positive integer as this UUID is less than, equal to, or greater than the specified UUID.
     * @since 1.0.0
     */
    override fun compareTo(other: Uuid): Int = value.compareTo(other.value)

    /**
     * Returns the string representation of the UUID.
     *
     * @return The UUID string value.
     * @since 1.0.0
     */
    override fun toString(): String = value

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other The object to compare with.
     * @return True if the other object is a [Uuid] with the same string value, false otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean = other is Uuid && other.value == value

    /**
     * Returns a hash code value for the object.
     *
     * @return The hash code of the UUID string value.
     * @since 1.0.0
     */
    override fun hashCode(): Int = value.hashCode()

    /**
     * Property delegate that returns the UUID string value.
     *
     * @param thisRef The object that owns the property.
     * @param property The property metadata.
     * @return The UUID string value.
     * @since 1.0.0
     */
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value

    /**
     * Factory methods for creating random UUIDs.
     *
     * @since 1.0.0
     */
    public companion object {
        /**
         * Generates a new random UUID v4.
         *
         * @return A new [UuidV4] instance.
         * @since 1.0.0
         */
        public fun randomUuidV4(): UuidV4 = UuidV4()

        /**
         * Generates a new random UUID v7.
         *
         * @return A new [UuidV7] instance.
         * @since 1.0.0
         */
        public fun randomUuidV7(): UuidV7 = UuidV7()
    }
}
