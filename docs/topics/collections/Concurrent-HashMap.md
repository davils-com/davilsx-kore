# Concurrent HashMap

`ConcurrentHashMap` is a high-performance, thread-safe map implementation for Kotlin Multiplatform projects. It is designed to work seamlessly with Coroutines, providing asynchronous, non-blocking operations.

## Key Features

- **Coroutines Native**: All operations are `suspend` functions.
- **Atomic Operations**: Support for complex atomic updates (`compute`, `merge`, etc.).
- **Mutex-backed**: Uses `kotlinx.coroutines.sync.Mutex` for fine-grained synchronization.
- **Functional API**: Integration with `Option<T>` to avoid nullability issues.

## Basic Usage

### Initialization

```kotlin
import com.davils.kore.collections.ConcurrentHashMap

val map = ConcurrentHashMap<String, Int>()
// or
val map = concurrentHashMapOf()
```

### Basic CRUD Operations

```kotlin
// Put a value
map.put("key", 1)

// Get a value (returns Option<Int>)
val value = map.get("key")
value.onSome { println("Found: $it") }

// Remove a value
val removed = map.remove("key")

// Check existence
val exists = map.containsKey("key")
```

## Atomic Operations

`ConcurrentHashMap` provides several methods to perform atomic updates, ensuring that no other coroutine can modify the entry while the transformation is in progress.

### Compute

The `compute` method allows you to update an entry based on its current value (if any).

```kotlin
import com.davils.kore.pattern.functional.Option

map.compute("counter") { current ->
    current.map { it + 1 }.or(Option.some(1))
}
```

### Put if Absent

```kotlin
map.putIfAbsent("key", 42)
```

### Merge

Merge a new value with an existing value using a remapping function.

```kotlin
map.merge("key", 10) { old, new ->
    old + new
}
```

## Views and Snapshots

Methods like `keys()`, `values()`, and `entries()` return a **snapshot** of the map at the time of the call. Subsequent changes to the map will not be reflected in the returned collection.

```kotlin
val allKeys = map.keys()
val allValues = map.values()
```
