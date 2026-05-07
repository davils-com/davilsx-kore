# JVM Specific Features

When running on the Java Virtual Machine (JVM), Kore provides additional features to simplify native library integration and system configuration.

## Native Bridge

The `NativeInterface` object provides a thread-safe way to load native libraries (JNI/C-Interop). It ensures that each library is only loaded once and provides a clean interface for initialization.

### Initialization

You can initialize one or more libraries using the `initialize` method:

```kotlin
import com.davils.kore.NativeInterface

// Single library
NativeInterface.initialize("mylib")

// Multiple libraries
NativeInterface.initialize("lib1", "lib2")
```

### Exception Handling

By default, `initialize` throws a `RuntimeException` if a library fails to load. You can provide a custom exception handler if needed:

```kotlin
NativeInterface.initialize("mylib") { error ->
    println("Failed to load library: ${error.message}")
}
```

## System Properties DSL

Kore offers an advanced DSL for managing JVM system properties through the `PropertiesScope`. This provides a more idiomatic and safer way to interact with `System.get/setProperty`.

### Usage

Use the `properties` function to open a scope:

```kotlin
import com.davils.kore.system.properties

properties {
    // Set a property (shorthand)
    this["app.mode"] = "production"
    
    // Set a property (explicit)
    setValue("app.version", "1.0.0")
    
    // Get a property
    val mode = getValueOrNull("app.mode")
    
    // Conditional setting
    setValueIfAbsent("app.debug", "false")
    
    // Transformation
    updateValue("counter") { current -> 
        val count = current?.toInt() ?: 0
        (count + 1).toString()
    }
}
```

## Environment Variables

The `Environment` object provides a common way to access environment variables. While available on all platforms, it is fully supported on the JVM.

```kotlin
import com.davils.kore.system.Environment

if (Environment.isSupported) {
    val dbUrl = Environment["DATABASE_URL"]
    println("Database URL: $dbUrl")
}
```

On non-supported platforms (like some web environments), `Environment.isSupported` will be `false`, and property access will return `null`.
