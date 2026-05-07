# Scoped Singleton

The `Singleton<out T>` interface provides a mechanism to ensure that only one instance of a particular type exists within a given scope. The Scoped Singleton is an implementation of the [Standard Singleton](Standard-Singleton.md), providing a thread-safe, lazily initialized instance.

## Overview

A Scoped Singleton ensures that the initialization logic is executed at most once, and the resulting instance is reused for all subsequent accesses. This is ideal for heavy objects like database connections, API clients, or shared configurations.

## Interface Definition

```kotlin
public interface Singleton<out T> {
    public val instance: T
}
```

## Usage

To create a scoped singleton, use the `singletonScoped` function. It uses `LazyThreadSafetyMode.SYNCHRONIZED` by default to ensure safety in multi-threaded environments.

### Basic Example

```kotlin
import com.davils.kore.pattern.creational.singleton.singletonScoped

val databaseSingleton = singletonScoped {
    Database.connect("jdbc:sqlite:data.db")
}

// Access the instance (initialized on first access)
val db = databaseSingleton.instance
```

## Use Cases

### Shared Resources
When multiple parts of your application need to access the same resource, a scoped singleton ensures they all use the same instance, reducing overhead.

```kotlin
val apiClient = singletonScoped {
    HttpClient.Builder()
        .baseUrl("https://api.example.com")
        .build()
}
```

### Dependency Injection
Scoped singletons can be used as a simple way to manage dependencies without a full DI framework.

```kotlin
class UserService(private val db: Database)

val userService = singletonScoped {
    UserService(databaseSingleton.instance)
}
```

---

## Why Use Scoped Singletons?

1.  **Lazy Initialization**: The instance is only created when it's actually needed.
2.  **Thread Safety**: Guaranteed safe access from multiple threads.
3.  **Memory Efficiency**: Only one instance is kept in memory.
4.  **Consistency**: Ensures the entire application works with the same stateful object.
