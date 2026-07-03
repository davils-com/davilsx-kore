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

package com.davils.kore.collections

import com.davils.kore.pattern.functional.Option

/**
 * A collection that supports concurrent access and modification.
 *
 * All operations are suspended to allow for non-blocking synchronization
 * across different coroutines and threads.
 *
 * @param T The type of elements held in this collection.
 * @since 1.2.0
 */
public interface ConcurrentCollection<T> {

    /**
     * Returns the number of elements in this collection.
     *
     * @return The number of elements.
     * @since 1.2.0
     */
    public suspend fun size(): Long

    /**
     * Removes all elements from this collection.
     *
     * @since 1.2.0
     */
    public suspend fun clear()

    /**
     * Returns true if the collection is empty (contains no elements).
     *
     * @return true if empty, false otherwise.
     * @since 1.2.0
     */
    public suspend fun isEmpty(): Boolean
}

/**
 * A key-value collection that supports concurrent access and modification.
 *
 * Provides operations to manipulate entries, keys, and values independently.
 *
 * @param K The type of keys.
 * @param V The type of values.
 * @since 1.2.0
 */
public interface ConcurrentMutableKVCollection<K, V>: ConcurrentCollection<Map.Entry<K, V>> {

    /**
     * Returns a set of all key-value entries in this collection.
     *
     * @return A set of entries.
     * @since 1.2.0
     */
    public suspend fun entries(): Set<Map.Entry<K, V>>

    /**
     * Returns a set of all keys in this collection.
     *
     * @return A set of keys.
     * @since 1.2.0
     */
    public suspend fun keys(): Set<K>

    /**
     * Returns a collection of all values in this collection.
     *
     * @return A collection of values.
     * @since 1.2.0
     */
    public suspend fun values(): Collection<V>

    /**
     * Associates the specified value with the specified key in this collection.
     *
     * @param key The key to associate with.
     * @param value The value to be associated.
     * @since 1.2.0
     */
    public suspend fun put(key: K, value: V)

    /**
     * Returns the value associated with the specified key, or [Option.None] if not found.
     *
     * @param key The key to look up.
     * @return An [Option] containing the associated value, or [Option.None].
     * @since 1.2.0
     */
    public suspend fun get(key: K): Option<V>

    /**
     * Removes the specified key and its associated value from this collection.
     *
     * @param key The key to remove.
     * @return An [Option] containing the value that was associated with the key, or [Option.None] if not found.
     * @since 1.2.0
     */
    public suspend fun remove(key: K): Option<V>

    /**
     * Returns true if this collection contains the specified key.
     *
     * @param key The key to check.
     * @return true if found, false otherwise.
     * @since 1.2.0
     */
    public suspend fun containsKey(key: K): Boolean

    /**
     * Returns true if this collection contains the specified value.
     *
     * @param value The value to check.
     * @return true if found, false otherwise.
     * @since 1.2.0
     */
    public suspend fun containsValue(value: V): Boolean

    /**
     * Returns the value to which the specified key is mapped, or [defaultValue] if this map contains no mapping for the key.
     *
     * @param key The key to look up.
     * @param defaultValue The default mapping of the key.
     * @return The value to which the specified key is mapped, or [defaultValue] if this map contains no mapping for the key.
     * @since 1.2.0
     */
    public suspend fun getOrDefault(key: K, defaultValue: V): V

    /**
     * If the specified key is not already associated with a value, associates it with the given value and returns [Option.None], else returns the current value.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return An [Option] containing the previous value associated with the specified key, or [Option.None] if there was no mapping for the key.
     * @since 1.2.0
     */
    public suspend fun putIfAbsent(key: K, value: V): Option<V>

    /**
     * Removes the entry for a key only if currently mapped to a given value.
     *
     * @param key The key with which the specified value is associated.
     * @param value The value expected to be associated with the specified key.
     * @return true if the value was removed, false otherwise.
     * @since 1.2.0
     */
    public suspend fun remove(key: K, value: V): Boolean

    /**
     * Replaces the entry for a key only if currently mapped to a given value.
     *
     * @param key The key with which the specified value is associated.
     * @param oldValue The value expected to be associated with the specified key.
     * @param newValue The value to be associated with the specified key.
     * @return true if the value was replaced, false otherwise.
     * @since 1.2.0
     */
    public suspend fun replace(key: K, oldValue: V, newValue: V): Boolean

    /**
     * Replaces the entry for a key only if currently mapped to some value.
     *
     * @param key The key with which the specified value is associated.
     * @param value The value to be associated with the specified key.
     * @return An [Option] containing the previous value associated with the specified key, or [Option.None] if there was no mapping for the key.
     * @since 1.2.0
     */
    public suspend fun replace(key: K, value: V): Option<V>

    /**
     * Copies all of the mappings from the specified map to this collection.
     *
     * @param from The map to copy mappings from.
     * @since 1.2.0
     */
    public suspend fun putAll(from: Map<out K, V>)

    /**
     * If the specified key is not already associated with a value, attempts to compute its value using the given mapping function and enters it into this map.
     *
     * The entire operation is performed atomically. The [mappingFunction] is invoked while holding a lock on the map.
     *
     * @param key The key with which the specified value is to be associated.
     * @param mappingFunction The function to compute a value.
     * @return The current (existing or computed) value associated with the specified key.
     * @since 1.2.0
     */
    public suspend fun computeIfAbsent(key: K, mappingFunction: suspend (K) -> V): V

    /**
     * If the value for the specified key is present, attempts to compute a new mapping given the key and its current mapped value.
     *
     * The entire operation is performed atomically. The [remappingFunction] is invoked while holding a lock on the map.
     * If the [remappingFunction] returns [Option.None], the mapping is removed.
     *
     * @param key The key with which the specified value is to be associated.
     * @param remappingFunction The function to compute a value.
     * @return An [Option] containing the new value associated with the specified key, or [Option.None] if none.
     * @since 1.2.0
     */
    public suspend fun computeIfPresent(key: K, remappingFunction: suspend (K, V) -> Option<V>): Option<V>

    /**
     * Attempts to compute a mapping for the specified key and its current mapped value (or [Option.None] if there is no current mapping).
     *
     * The entire operation is performed atomically. The [remappingFunction] is invoked while holding a lock on the map.
     * If the [remappingFunction] returns [Option.None], the mapping is removed (or remains absent).
     *
     * @param key The key with which the specified value is to be associated.
     * @param remappingFunction The function to compute a value.
     * @return An [Option] containing the new value associated with the specified key, or [Option.None] if none.
     * @since 1.2.0
     */
    public suspend fun compute(key: K, remappingFunction: suspend (K, Option<V>) -> Option<V>): Option<V>

    /**
     * If the specified key is not already associated with a value or is associated with [Option.None], associates it with the given non-null value.
     * Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is [Option.None].
     *
     * The entire operation is performed atomically. The [remappingFunction] is invoked while holding a lock on the map.
     *
     * @param key The key with which the resulting value is to be associated.
     * @param value The non-null value to be merged with the existing value associated with the key or, if no existing value is associated with the key, to be associated with the key.
     * @param remappingFunction The function to recompute a value if present.
     * @return An [Option] containing the new value associated with the specified key, or [Option.None] if no value is associated with the key.
     * @since 1.2.0
     */
    public suspend fun merge(key: K, value: V, remappingFunction: suspend (V, V) -> Option<V>): Option<V>
}

/**
 * A mutable collection that supports concurrent access and modification.
 *
 * @param T The type of elements held in this collection.
 * @since 1.2.0
 */
public interface ConcurrentMutableCollection<T>: ConcurrentCollection<T> {

    /**
     * Adds the specified element to this collection.
     *
     * @param element The element to add.
     * @return true if the element was added, false if it was already present.
     * @since 1.2.0
     */
    public suspend fun add(element: T): Boolean

    /**
     * Removes the specified element from this collection.
     *
     * @param element The element to remove.
     * @return true if the element was removed, false if it was not present.
     * @since 1.2.0
     */
    public suspend fun remove(element: T): Boolean
}
