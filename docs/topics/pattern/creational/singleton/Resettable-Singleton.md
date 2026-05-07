# Resettable Singleton

A `ResettableSingleton` is an extension of the [Standard Singleton](Standard-Singleton.md) that allows you to clear the current instance, forcing a re-initialization on the next access. This is useful for managing state that needs to be discarded and recreated, such as user sessions, caches, or temporary configurations.

## Overview

While standard singletons are immutable once initialized, the Resettable Singleton provides a `reset()` method. When called, the current instance is dropped, and the next call to `instance` will trigger the initializer again.

## Interface Definition

```kotlin
public interface ResettableSingleton<out T> : Singleton<T> {
    public fun reset()
}
```

## Usage

Use the `resettableSingleton` function to create an instance. It is thread-safe for both initialization and resetting.

### Basic Example

```kotlin
import com.davils.kore.pattern.creational.singleton.resettableSingleton

val userSession = resettableSingleton {
    Session.loadFromDisk()
}

// Use session
val current = userSession.instance

// Clear session (e.g., on logout)
userSession.reset()

// Next access re-loads the session
val newSession = userSession.instance
```

## Use Cases

### User Sessions
In applications with login/logout functionality, a resettable singleton can hold the current user's session. When the user logs out, the singleton is reset so the next login can initialize a fresh session.

### Cache Management
If you have a singleton that caches data from an external source, you can use `reset()` to force a refresh of the cached data without having to recreate the entire singleton container.

### Testing
Resettable singletons are excellent for testing. You can reset global state between test cases to ensure test isolation.

```kotlin
@BeforeTest
fun setup() {
    GlobalState.reset()
}
```

---

## Why Use Resettable Singletons?

1.  **State Management**: Explicitly control the lifecycle of a singleton instance.
2.  **Memory Management**: Free up resources by resetting large objects when they are no longer needed.
3.  **Thread-Safe Reset**: Safely reset the instance even when multiple threads are accessing it.
4.  **Testability**: Avoid side effects between tests by clearing shared state.
