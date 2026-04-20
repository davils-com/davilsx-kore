# Asynchronous Factory

The `FactoryAsync<out R>` is the asynchronous counterpart to the standard `Factory`. It is designed to work with Kotlin Coroutines, providing an interface for asynchronous object creation.

## Interface Definition

```kotlin
public fun interface FactoryAsync<out R> {
    public suspend fun create(): R

    public suspend fun createResult(): Result<R>
    public suspend fun createOrNull(): R?
    public suspend fun createIf(predicate: suspend (R) -> Boolean): R?
}
```

## Basic Usage

To define an asynchronous factory, you can use a lambda expression with the `suspend` keyword or implement the interface.

```kotlin
import com.davils.kore.pattern.creational.factory.FactoryAsync

// A factory that fetches a user from a network API
val remoteUserFactory = FactoryAsync {
    fetchUserFromNetwork() // suspending call
}

suspend fun main() {
    // Create an instance asynchronously
    val user = remoteUserFactory.create()
}
```

## Safe Creation and Cancellation

The `FactoryAsync` interface handles asynchronous errors and supports coroutine cancellation by default.

- **`create()`**: Asynchronously creates the instance.
- **`createResult()`**: Returns a `Result<R>`, capturing any `Exception`. It ensures that `CancellationException` is correctly rethrown.
- **`createOrNull()`**: Returns `null` if any exception (except `CancellationException`) occurs during the creation.
- **`createIf(predicate)`**: Creates the instance and then evaluates it with a suspending predicate.

### Cancellation Safety

One of the key benefits of `FactoryAsync` is its "coroutine-friendly" design. It ensures that structured concurrency is preserved by rethrowing `CancellationException` in all safe creation methods. This means that if the coroutine scope that called the factory is canceled, the factory will propagate that cancellation.

## Transformations

Asynchronous factories support both synchronous and asynchronous transformations.

### map

Use `map` for simple synchronous transformations of the created object.

```kotlin
val idFactory = remoteUserFactory.map { it.id }
```

### mapAsync

Use `mapAsync` for transformations that require further asynchronous calls.

```kotlin
val profileFactory = remoteUserFactory.mapAsync { user ->
    fetchProfileForUser(user.id) // suspending call
}
```

---

## Why Use Asynchronous Factories?

1.  **I/O Operations**: Ideal for network calls, database queries, or file reading during object creation.
2.  **Concurrency Support**: Built-in support for coroutines and cancellation.
3.  **Pipeline Integration**: Can be easily integrated into complex asynchronous data processing pipelines.
