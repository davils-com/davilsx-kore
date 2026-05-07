# Value Providers

The `ValueProvider` interface is a generic abstraction used across Kore and the DavilsX ecosystem for retrieving values by key. It provides a consistent way to access configuration values, environment variables, or any other key-value based data.

## The ValueProvider Interface

The interface `com.davils.kore.ValueProvider<T>` defines several methods for safe and convenient value retrieval.

### Basic Retrieval

*   `getValueOrNull(key)`: Returns the value or `null` if not found.
*   `getValueOrThrow(key)`: Returns the value or throws a `NoSuchElementException`.
*   `getValueOrDefault(key, default)`: Returns the value or a provided default.

### Functional API

Value providers support functional patterns for working with values:

*   `getValueResult(key)`: Returns the value wrapped in a `Result`.
*   `useValue(key) { value -> ... }`: Executes a block with the retrieved value.
*   `mapValue(key) { value -> ... }`: Transforms the retrieved value.

### Operators

You can use the index operator and the `in` operator with value providers:

```kotlin
val provider: ValueProvider<String> = // ...
val value = provider["my.key"]
if ("my.key" in provider) {
    // ...
}
```

## Implementations in Kore

Kore provides several implementations of `ValueProvider`:

### Environment

The `Environment` object implements `ValueProvider<String>`, providing access to system environment variables across platforms.

```kotlin
import com.davils.kore.system.Environment

val path = Environment["PATH"]
```

### PropertiesScope (JVM)

The `PropertiesScope` used in the `properties { }` DSL also implements `ValueProvider<String>` for JVM system properties.

```kotlin
import com.davils.kore.system.properties

properties {
    val version = getValueOrDefault("java.version", "unknown")
}
```

## Creating Custom Providers

You can easily create your own value providers by implementing the `ValueProvider` interface. You only need to implement `getValueOrNull(key)`.

```kotlin
class MyConfigProvider(private val config: Map<String, String>) : ValueProvider<String> {
    override fun getValueOrNull(key: String): String? = config[key]
}
```
