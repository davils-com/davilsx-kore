# Standard Factory

The `Factory<out R>` is the core functional interface for creating instances of type `R` synchronously. It follows the classic Factory pattern, providing a clean abstraction for object creation.

## Interface Definition

```kotlin
public fun interface Factory<out R> {
    public fun create(): R

    public fun createResult(): Result<R>
    public fun createOrNull(): R?
    public fun createIf(predicate: (R) -> Boolean): R?
}
```

## Basic Usage

To define a factory, you can use a lambda expression or implement the interface.

```kotlin
import com.davils.kore.pattern.creational.factory.Factory

// Define a simple factory using a lambda
val greetingFactory = Factory { "Hello, Kore!" }

// Create an instance
val message = greetingFactory.create()
```

## Safe Creation Methods

The `Factory` interface includes built-in methods to handle potential errors during creation:

- **`create()`**: The standard way to create an object. Throws an exception if the creation logic fails.
- **`createResult()`**: Wraps the creation in a `Result` type, catching any exceptions and returning a `Success` or `Failure`.
- **`createOrNull()`**: Returns `null` if the creation process throws any exception.
- **`createIf(predicate)`**: Creates the object and then checks it against a predicate. Returns the object if it satisfies the condition, otherwise `null`.

### Example: Error Handling

```kotlin
val safeMessage = greetingFactory.createOrNull()

val conditionalMessage = greetingFactory.createIf { it.isNotBlank() }
```

## Transformations

Factories can be transformed using the `map` extension function. This allows you to create new factories by applying a transformation to the output of an existing one.

```kotlin
val intFactory = Factory { 42 }
val stringFactory = intFactory.map { "The answer is: $it" }

val message = stringFactory.create() // "The answer is: 42"
```

---

## Why Use Standard Factories?

1.  **Decoupling**: The caller doesn't need to know how the object is constructed.
2.  **Testability**: Easily swap real implementations with mocks or stubs.
3.  **Consistency**: Use the same interface for all object creation logic.
4.  **Advanced Usage**: See [Singleton Factories](Singleton.md) or [Inheritance](Inheritance.md) for more details.
