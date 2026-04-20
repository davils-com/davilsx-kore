# Asynchronous Parameterized Factory

The `FactoryParameterizedAsync<P, R>` interface allows for asynchronous object creation that depends on an input parameter. It combines the power of parameterized factories with Kotlin Coroutines.

## Interface Definition

```kotlin
public fun interface FactoryParameterizedAsync<P : FactoryParameter, out R> {
    public suspend fun create(parameter: P): R

    public suspend fun createResult(parameter: P): Result<R>
    public suspend fun createOrNull(parameter: P): R?
    public suspend fun createIf(parameter: P, predicate: (P, R) -> Boolean): R?
}
```

## Basic Usage

To define an asynchronous parameterized factory, you can use a lambda expression with the `suspend` keyword.

```kotlin
import com.davils.kore.pattern.creational.factory.FactoryParameterizedAsync

val remoteUserFactory = FactoryParameterizedAsync<UserConfig, User> { config ->
    // Suspending call using the provided parameter
    fetchUserFromServer(config.id)
}

suspend fun main() {
    val user = remoteUserFactory.create(UserConfig("123", "MEMBER"))
}
```

## Safe Creation and Cancellation

The `FactoryParameterizedAsync` interface handles asynchronous errors and supports coroutine cancellation by default.

- **`create(parameter)`**: Asynchronously creates the instance using the parameter.
- **`createResult(parameter)`**: Returns a `Result<R>`, capturing any `Exception`. It ensures that `CancellationException` is correctly rethrown.
- **`createOrNull(parameter)`**: Returns `null` if any exception (except `CancellationException`) occurs during the creation.
- **`createIf(parameter, predicate)`**: Creates the instance and then evaluates it with a predicate that has access to both the parameter and the result.

## Transformations

Asynchronous parameterized factories support both synchronous and asynchronous transformations.

### map

Use `map` for simple synchronous transformations.

```kotlin
val nameFactory = remoteUserFactory.map { it.name }
```

### mapAsync

Use `mapAsync` for transformations that require further asynchronous calls.

```kotlin
val detailsFactory = remoteUserFactory.mapAsync { user ->
    fetchDetailedProfile(user.id) // suspending call
}
```

---

## Why Use Asynchronous Parameterized Factories?

1.  **Dynamic Network Requests**: Fetch data from an API based on a configuration object.
2.  **Resource Loading**: Load specific resources (like files or database entries) asynchronously based on a parameter.
3.  **Pipeline Abstraction**: Define complex, parameter-dependent asynchronous creation pipelines.
