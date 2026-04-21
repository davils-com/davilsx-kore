# Standard Parameterized Factory

The `FactoryParameterized<P, R>` interface extends the factory pattern by allowing you to pass a parameter during the object creation process. This is essential when the output depends on dynamic configuration or input.

## FactoryParameter Interface

All parameters used with parameterized factories must implement the `FactoryParameter` marker interface. This ensures consistency and type safety.

```kotlin
import com.davils.kore.pattern.creational.factory.FactoryParameter

data class UserConfig(val id: String, val role: String) : FactoryParameter
```

## Interface Definition

```kotlin
public fun interface FactoryParameterized<P : FactoryParameter, out R> {
    public fun create(parameter: P): R

    public fun createResult(parameter: P): Result<R>
    public fun createOrNull(parameter: P): R?
    public fun createIf(parameter: P, predicate: (P, R) -> Boolean): R?
}
```

## Basic Usage

```kotlin
import com.davils.kore.pattern.creational.factory.FactoryParameterized

val userFactory = FactoryParameterized<UserConfig, User> { config ->
    User(id = config.id, role = config.role)
}

val admin = userFactory.create(UserConfig("1", "ADMIN"))
```

## Safe Creation Methods

Just like the standard factory, the parameterized version includes safety features that accept the required parameter:

- **`create(parameter)`**: Standard creation using the given parameter.
- **`createResult(parameter)`**: Returns a `Result<R>` using the parameter.
- **`createOrNull(parameter)`**: Returns `null` if the creation fails for the given parameter.
- **`createIf(parameter, predicate)`**: Creates the object and checks it against a predicate that has access to both the parameter and the created result.

## Transformations

When mapping a parameterized factory, the requirement for the original parameter type is preserved.

```kotlin
val idFactory = FactoryParameterized<UserConfig, String> { it.id }

// map preserves the <UserConfig> parameter requirement
val logFactory = idFactory.map { "Log: User with ID $it created" }

val logEntry = logFactory.create(UserConfig("42", "USER"))
```

---

## Why Use Parameterized Factories?

1.  **Dynamic Input**: When object state depends on runtime data.
2.  **Configurations**: For creating objects based on complex configuration data classes.
3.  **Registry Systems**: Useful when a parameter acts as a key to determine the specific implementation created.
4.  **Advanced Usage**: See [Dynamic Factories](Dynamic.md) for real-world examples.
