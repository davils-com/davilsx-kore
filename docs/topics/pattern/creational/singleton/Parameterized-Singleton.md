# Parameterized Singleton

The `SingletonParameterized<out T, in P>` interface allows you to retrieve a singleton instance based on a provided parameter. It is the singleton counterpart to the `FactoryParameterized` and complements the [Standard Singleton](Standard-Singleton.md) pattern for keyed instances.

## Interface Definition

```kotlin
public fun interface SingletonParameterized<out T, in P : FactoryParameter> {
    public fun getInstance(parameter: P): T

    public fun getInstanceResult(parameter: P): Result<T>
    public fun getInstanceOrNull(parameter: P): T?
}
```

## Basic Usage

All parameters used with parameterized singletons must implement the `FactoryParameter` marker interface.

```kotlin
import com.davils.kore.pattern.creational.singleton.singletonParameterized
import com.davils.kore.pattern.creational.factory.FactoryParameter

data class UserKey(val id: String) : FactoryParameter

val userCache = singletonParameterized<User, UserKey> { key ->
    User.fetchFromDatabase(key.id)
}

// Retrieves (and potentially creates) the instance for the specific key
val user = userCache.getInstance(UserKey("42"))
```

## Safe Retrieval Methods

Parameterized singletons include built-in methods to handle potential errors during retrieval:

- **`getInstance(parameter)`**: The standard way to retrieve an instance.
- **`getInstanceResult(parameter)`**: Wraps the retrieval in a `Result` type.
- **`getInstanceOrNull(parameter)`**: Returns `null` if the retrieval fails.

---

## Why Use Parameterized Singletons?

1.  **Caching**: Ideal for implementing simple caches where an instance is tied to a specific key.
2.  **Consistency**: Ensures that for a given parameter, you always work with the same instance (depending on implementation).
3.  **Type Safety**: Uses the `FactoryParameter` interface to ensure parameters are well-defined.
