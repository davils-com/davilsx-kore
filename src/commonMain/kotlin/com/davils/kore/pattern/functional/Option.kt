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

package com.davils.kore.pattern.functional

/**
 * Represents an optional value that may or may not be present.
 *
 * [Option] is a sealed class with two variants: [Some], which contains a value,
 * and [None], which represents the absence of a value.
 *
 * This type is inspired by Rust's `Option` type and provides a functional
 * alternative to nullable types.
 *
 * @param T The type of the value held by this option.
 * @since 1.2.0
 */
public sealed class Option<out T> {

    /**
     * Returns true if the option is [Some].
     *
     * @return true if the option contains a value, false otherwise.
     * @since 1.2.0
     */
    public abstract fun isSome(): Boolean

    /**
     * Returns true if the option is [None].
     *
     * @return true if the option does not contain a value, false otherwise.
     * @since 1.2.0
     */
    public abstract fun isNone(): Boolean

    /**
     * Returns the value if the option is [Some], otherwise returns null.
     *
     * @return The value or null.
     * @since 1.2.0
     */
    public abstract fun getOrNull(): T?

    /**
     * Returns the value if the option is [Some], otherwise returns the result of [defaultValue].
     *
     * @param defaultValue A function that returns a default value.
     * @return The value or the default value.
     * @since 1.2.0
     */
    public inline fun getOrElse(defaultValue: () -> @UnsafeVariance T): T {
        return when (this) {
            is Some -> value
            is None -> defaultValue()
        }
    }

    /**
     * Returns [other] if the option is [Some], otherwise returns [None].
     *
     * @param U The type of the other option.
     * @param other The other option.
     * @return [other] or [None].
     * @since 1.2.0
     */
    public fun <U> and(other: Option<U>): Option<U> {
        return when (this) {
            is Some -> other
            is None -> None
        }
    }

    /**
     * Returns the option if it is [Some], otherwise returns [other].
     *
     * @param other The alternative option.
     * @return The option or [other].
     * @since 1.2.0
     */
    public fun or(other: Option<@UnsafeVariance T>): Option<T> {
        return when (this) {
            is Some -> this
            is None -> other
        }
    }

    /**
     * Returns the value if the option is [Some], otherwise throws an exception.
     *
     * @param exception A function that returns an exception to throw.
     * @return The value.
     * @throws Throwable The exception returned by [exception].
     * @since 1.2.0
     */
    public inline fun getOrThrow(exception: () -> Throwable): T {
        return when (this) {
            is Some -> value
            is None -> throw exception()
        }
    }

    /**
     * Maps the value of the option if it is [Some] using the provided [transform] function.
     *
     * @param U The type of the transformed value.
     * @param transform The function to apply to the value.
     * @return A new [Option] containing the transformed value, or [None].
     * @since 1.2.0
     */
    public inline fun <U> map(transform: (T) -> U): Option<U> {
        return when (this) {
            is Some -> Some(transform(value))
            is None -> None
        }
    }

    /**
     * Applies the provided [transform] function to the value of the option if it is [Some]
     * and returns the resulting [Option].
     *
     * @param U The type of the value in the resulting option.
     * @param transform The function to apply to the value.
     * @return The result of [transform], or [None].
     * @since 1.2.0
     */
    public inline fun <U> flatMap(transform: (T) -> Option<U>): Option<U> {
        return when (this) {
            is Some -> transform(value)
            is None -> None
        }
    }

    /**
     * Returns the option if it is [Some] and the value matches the [predicate],
     * otherwise returns [None].
     *
     * @param predicate The condition to check.
     * @return The option or [None].
     * @since 1.2.0
     */
    public inline fun filter(predicate: (T) -> Boolean): Option<T> {
        return when (this) {
            is Some -> if (predicate(value)) this else None
            is None -> None
        }
    }

    /**
     * Folds the option into a value of type [R] by applying [ifSome] if it is [Some]
     * or returning the result of [ifNone] if it is [None].
     *
     * @param R The type of the result.
     * @param ifNone A function to call if the option is [None].
     * @param ifSome A function to apply to the value if the option is [Some].
     * @return The result of [ifNone] or [ifSome].
     * @since 1.2.0
     */
    public inline fun <R> fold(ifNone: () -> R, ifSome: (T) -> R): R {
        return when (this) {
            is Some -> ifSome(value)
            is None -> ifNone()
        }
    }

    /**
     * Performs the given [action] on the encapsulated value if the option is [Some].
     *
     * @param action The action to perform.
     * @return The original [Option].
     * @since 1.2.0
     */
    public inline fun onSome(action: (T) -> Unit): Option<T> {
        if (this is Some) action(value)
        return this
    }

    /**
     * Performs the given [action] if the option is [None].
     *
     * @param action The action to perform.
     * @return The original [Option].
     * @since 1.2.0
     */
    public inline fun onNone(action: () -> Unit): Option<T> {
        if (this is None) action()
        return this
    }

    /**
     * Returns true if the option is [Some] and the value matches the [predicate].
     *
     * @param predicate The condition to check.
     * @return true if the condition is met, false otherwise.
     * @since 1.2.0
     */
    public inline fun isSomeAnd(predicate: (T) -> Boolean): Boolean {
        return this is Some && predicate(value)
    }

    /**
     * Returns true if the option is [None] or the value matches the [predicate].
     *
     * @param predicate The condition to check.
     * @return true if the option is [None] or the condition is met, false otherwise.
     * @since 1.2.0
     */
    public inline fun isNoneOr(predicate: (T) -> Boolean): Boolean {
        return this is None || (this is Some && predicate(value))
    }

    /**
     * Represents the presence of a value.
     *
     * This variant contains a non-null value of type [T].
     *
     * @param T The type of the value.
     * @since 1.2.0
     */
    public data class Some<out T>(
        /**
         * The contained value.
         *
         * @since 1.2.0
         */
        val value: T
    ) : Option<T>() {
        override fun isSome(): Boolean = true
        override fun isNone(): Boolean = false
        override fun getOrNull(): T = value
    }

    /**
     * Represents the absence of a value.
     *
     * @since 1.2.0
     */
    public object None : Option<Nothing>() {
        override fun isSome(): Boolean = false
        override fun isNone(): Boolean = true
        override fun getOrNull(): Nothing? = null
    }

    public companion object {
        /**
         * Creates an [Option] from a nullable value.
         *
         * Returns [Some] if the value is not null, otherwise returns [None].
         *
         * @param T The type of the value.
         * @param value The value to wrap.
         * @return [Some] if value is not null, [None] otherwise.
         * @since 1.2.0
         */
        public fun <T> fromNullable(value: T?): Option<T> {
            return if (value != null) Some(value) else None
        }

        /**
         * Creates a [Some] variant with the given [value].
         *
         * @param T The type of the value.
         * @param value The value to wrap.
         * @return A [Some] instance.
         * @since 1.2.0
         */
        public fun <T> some(value: T): Option<T> = Some(value)

        /**
         * Returns the [None] variant.
         *
         * @return The [None] instance.
         * @since 1.2.0
         */
        public fun none(): Option<Nothing> = None
    }
}

/**
 * Converts a nullable value to an [Option].
 *
 * @param T The type of the value.
 * @return [Option.Some] if value is not null, [Option.None] otherwise.
 * @since 1.2.0
 */
public fun <T> T?.toOption(): Option<T> = Option.fromNullable(this)
