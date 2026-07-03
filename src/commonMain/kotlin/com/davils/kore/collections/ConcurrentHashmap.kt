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

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A thread-safe hash map implementation using [Mutex] for synchronization.
 *
 * This class provides a concurrent version of a hash map where all operations
 * are suspended. It ensures that only one coroutine can modify or access
 * the underlying map at a time.
 *
 * @param K The type of keys.
 * @param V The type of values.
 * @since 1.2.0
 */
public class ConcurrentHashMap<K, V>: ConcurrentMutableKVCollection<K, V> {
    private val map: MutableMap<K, V> = mutableMapOf()
    private val mutex: Mutex = Mutex()

    /**
     * Returns the number of elements in this map.
     *
     * @return The number of elements.
     * @since 1.2.0
     */
    public override suspend fun size(): Long = mutex.withLock { map.size.toLong() }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key The key to associate with.
     * @param value The value to be associated.
     * @since 1.2.0
     */
    public override suspend fun put(key: K, value: V): Unit = mutex.withLock { map[key] = value }

    /**
     * Returns the value associated with the specified key, or null if not found.
     *
     * @param key The key to look up.
     * @return The associated value, or null.
     * @since 1.2.0
     */
    public override suspend fun get(key: K): V? = mutex.withLock { map[key] }

    /**
     * Removes the specified key and its associated value from this map.
     *
     * @param key The key to remove.
     * @return The value that was associated with the key, or null if not found.
     * @since 1.2.0
     */
    public override suspend fun remove(key: K): V? = mutex.withLock { map.remove(key) }

    /**
     * Returns true if this map contains the specified key.
     *
     * @param key The key to check.
     * @return true if found, false otherwise.
     * @since 1.2.0
     */
    public override suspend fun containsKey(key: K): Boolean = mutex.withLock { map.containsKey(key) }

    /**
     * Returns true if this map contains the specified value.
     *
     * @param value The value to check.
     * @return true if found, false otherwise.
     * @since 1.2.0
     */
    public override suspend fun containsValue(value: V): Boolean = mutex.withLock { map.containsValue(value) }

    /**
     * Returns true if the map is empty.
     *
     * @return true if empty, false otherwise.
     * @since 1.2.0
     */
    public override suspend fun isEmpty(): Boolean = mutex.withLock { map.isEmpty() }

    /**
     * Returns a snapshot of all key-value entries in this map.
     *
     * @return A set of entries.
     * @since 1.2.0
     */
    public override suspend fun entries(): Set<Map.Entry<K, V>> = mutex.withLock { map.entries.toSet() }

    /**
     * Returns a snapshot of all keys in this map.
     *
     * @return A set of keys.
     * @since 1.2.0
     */
    public override suspend fun keys(): Set<K> = mutex.withLock { map.keys.toSet() }

    /**
     * Returns a snapshot of all values in this map.
     *
     * @return A collection of values.
     * @since 1.2.0
     */
    public override suspend fun values(): Collection<V> = mutex.withLock { map.values.toList() }

    /**
     * Removes all elements from this map.
     *
     * @since 1.2.0
     */
    public override suspend fun clear(): Unit = mutex.withLock { map.clear() }

    /**
     * Returns the value to which the specified key is mapped, or [defaultValue] if this map contains no mapping for the key.
     *
     * @param key The key to look up.
     * @param defaultValue The default mapping of the key.
     * @return The value to which the specified key is mapped, or [defaultValue] if this map contains no mapping for the key.
     * @since 1.2.0
     */
    public override suspend fun getOrDefault(key: K, defaultValue: V): V = mutex.withLock {
        map[key] ?: defaultValue
    }

    /**
     * If the specified key is not already associated with a value, associates it with the given value and returns null, else returns the current value.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with the specified key, or null if there was no mapping for the key.
     * @since 1.2.0
     */
    public override suspend fun putIfAbsent(key: K, value: V): V? = mutex.withLock {
        if (!map.containsKey(key)) {
            map.put(key, value)
        } else {
            map[key]
        }
    }

    /**
     * Removes the entry for a key only if currently mapped to a given value.
     *
     * @param key The key with which the specified value is associated.
     * @param value The value expected to be associated with the specified key.
     * @return true if the value was removed, false otherwise.
     * @since 1.2.0
     */
    public override suspend fun remove(key: K, value: V): Boolean = mutex.withLock {
        if (map.containsKey(key) && map[key] == value) {
            map.remove(key)
            true
        } else {
            false
        }
    }

    /**
     * Replaces the entry for a key only if currently mapped to a given value.
     *
     * @param key The key with which the specified value is associated.
     * @param oldValue The value expected to be associated with the specified key.
     * @param newValue The value to be associated with the specified key.
     * @return true if the value was replaced, false otherwise.
     * @since 1.2.0
     */
    public override suspend fun replace(key: K, oldValue: V, newValue: V): Boolean = mutex.withLock {
        if (map.containsKey(key) && map[key] == oldValue) {
            map[key] = newValue
            true
        } else {
            false
        }
    }

    /**
     * Replaces the entry for a key only if currently mapped to some value.
     *
     * @param key The key with which the specified value is associated.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with the specified key, or null if there was no mapping for the key.
     * @since 1.2.0
     */
    public override suspend fun replace(key: K, value: V): V? = mutex.withLock {
        if (map.containsKey(key)) {
            map.put(key, value)
        } else {
            null
        }
    }

    /**
     * Copies all of the mappings from the specified map to this collection.
     *
     * @param from The map to copy mappings from.
     * @since 1.2.0
     */
    public override suspend fun putAll(from: Map<out K, V>): Unit = mutex.withLock {
        map.putAll(from)
    }

    /**
     * If the specified key is not already associated with a value, attempts to compute its value using the given mapping function and enters it into this map unless null.
     *
     * @param key The key with which the specified value is to be associated.
     * @param mappingFunction The function to compute a value.
     * @return The current (existing or computed) value associated with the specified key, or null if the computed value is null.
     * @since 1.2.0
     */
    public override suspend fun computeIfAbsent(key: K, mappingFunction: suspend (K) -> V): V = mutex.withLock {
        val current = map[key]
        if (current != null) return@withLock current

        val newValue = mappingFunction(key)
        map[key] = newValue
        newValue
    }

    /**
     * If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
     *
     * @param key The key with which the specified value is to be associated.
     * @param remappingFunction The function to compute a value.
     * @return The new value associated with the specified key, or null if none.
     * @since 1.2.0
     */
    public override suspend fun computeIfPresent(key: K, remappingFunction: suspend (K, V) -> V?): V? = mutex.withLock {
        val oldValue = map[key] ?: return@withLock null
        val newValue = remappingFunction(key, oldValue)
        if (newValue != null) {
            map[key] = newValue
        } else {
            map.remove(key)
        }
        newValue
    }

    /**
     * Attempts to compute a mapping for the specified key and its current mapped value (or null if there is no current mapping).
     *
     * @param key The key with which the specified value is to be associated.
     * @param remappingFunction The function to compute a value.
     * @return The new value associated with the specified key, or null if none.
     * @since 1.2.0
     */
    public override suspend fun compute(key: K, remappingFunction: suspend (K, V?) -> V?): V? = mutex.withLock {
        val oldValue = map[key]
        val newValue = remappingFunction(key, oldValue)
        if (newValue != null) {
            map[key] = newValue
        } else {
            map.remove(key)
        }
        newValue
    }

    /**
     * If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value.
     * Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is null.
     *
     * @param key The key with which the resulting value is to be associated.
     * @param value The non-null value to be merged with the existing value associated with the key or, if no existing value or a null value is associated with the key, to be associated with the key.
     * @param remappingFunction The function to recompute a value if present.
     * @return The new value associated with the specified key, or null if no value is associated with the key.
     * @since 1.2.0
     */
    public override suspend fun merge(key: K, value: V, remappingFunction: suspend (V, V) -> V?): V? = mutex.withLock {
        val oldValue = map[key]
        val newValue = if (oldValue == null) value else remappingFunction(oldValue, value)
        if (newValue == null) {
            map.remove(key)
        } else {
            map[key] = newValue
        }
        newValue
    }
}

public fun <K, V> concurrentHashMapOf(): ConcurrentHashMap<K, V> = ConcurrentHashMap()
