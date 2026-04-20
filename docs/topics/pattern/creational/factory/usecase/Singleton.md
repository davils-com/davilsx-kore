# Singleton Factory with object

When you only need a single instance of a factory throughout your application, using a Kotlin `object` is the most efficient way. This is ideal for static object creation logic that doesn't require any external state.

## Implementation

To create a singleton factory, simply implement the `Factory` or `FactoryAsync` interface in a Kotlin `object`.

```kotlin
import com.davils.kore.pattern.creational.factory.Factory

/**
 * A singleton factory for creating default configurations.
 */
object DefaultConfigFactory : Factory<Configuration> {
    override fun create(): Configuration {
        return Configuration(
            timeout = 3000,
            retryCount = 3,
            enableLogging = true
        )
    }
}
```

## Usage

Since it's an `object`, you can access it directly without instantiation.

```kotlin
// Access the singleton factory directly
val config = DefaultConfigFactory.create()
```

---

## When to use this?

- **Global Defaults**: When you have a set of default values or settings used across the entire app.
- **Static Configurations**: For objects that don't change based on user input or runtime environment.
- **Stateless Logic**: When the creation process doesn't depend on any external dependencies or state.
