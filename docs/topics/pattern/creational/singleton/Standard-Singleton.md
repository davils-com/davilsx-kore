# Standard Singleton

The `Singleton<out T>` is the base interface for all singleton patterns in the Kore library. It provides a consistent way to access a single, shared instance of a specific type.

## Interface Definition

```kotlin
public interface Singleton<out T> {
    public val instance: T
}
```

## Basic Concept

Unlike a standard Kotlin `object`, the `Singleton` interface allows for more flexible implementations, such as lazy initialization, resettable states, or scoped instances, while maintaining a common access pattern through the `instance` property.

## Implementations

The library provides several specialized implementations of the singleton pattern:

- **[Scoped Singleton](Scoped-Singleton.md)**: A thread-safe, lazily initialized singleton (the most common use case).
- **[Resettable Singleton](Resettable-Singleton.md)**: A singleton that can be cleared and re-initialized, useful for state management or testing.
- **[Parameterized Singleton](Parameterized-Singleton.md)**: Manages unique instances based on an input parameter (like a cache).

## Why Use the Singleton Interface?

1.  **Abstraction**: Decouples the retrieval logic from the usage.
2.  **Consistency**: Provides a uniform API (`.instance`) for all shared components.
3.  **Testability**: Interfaces are easier to mock or replace in unit tests compared to global objects.
4.  **Flexibility**: Allows switching between different singleton behaviors (e.g., from Scoped to Resettable) without changing the call sites.

---

## See Also

- [Factory Pattern](Standard-Factory.md)
- [Scoped Singleton](Scoped-Singleton.md)
