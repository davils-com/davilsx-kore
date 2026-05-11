# System Properties (JVM)

<primary-label ref="annotation"/>

Kore provides a powerful DSL for managing JVM system properties, allowing you to read, write, and remove properties with ease.

## Overview

The `PropertiesScope` is a JVM-specific utility that wraps `System.getProperties()` and related methods in a clean, type-safe API. It is particularly useful for configuration and runtime environment adjustments.

### Key Features

- **Fluent DSL**: Readable syntax for property management.
- **Write Operations**: Set single or multiple properties with success verification.
- **Safe Operations**: Built-in exception handling for property access.
- **Validation**: Throwing or conditional setters to ensure state consistency.

## Basic Usage

You can access system properties using the `props` property or the `properties()` function.

```kotlin
import com.davils.kore.system.props

// Reading properties
val osName = props.getValueOrNull("os.name")
val userHome = props.getValueOrThrow("user.home")

// Writing properties using index operator
props["my.custom.prop"] = "hello-world"
```

## Properties DSL

The `properties` builder provides a scoped environment for multiple property operations.

```kotlin
import com.davils.kore.system.properties

properties {
    // Conditional update
    setValueIfAbsent("app.mode", "production")
    
    // Bulk update
    setAllValues(mapOf(
        "debug.enabled" to "true",
        "log.level" to "INFO"
    ))
    
    // Removing a property
    removeValue("temp.secret")
}
```

## Available Operations

Inside a `properties` block, you have access to a rich set of methods:

### Retrieval

| Method | Description |
|--------|-------------|
| `getValueOrNull(key)` | Safely retrieves a property value. |
| `getValueOrThrow(key)` | Retrieves a value or throws `IllegalStateException`. |

### Modification

| Method | Description |
|--------|-------------|
| `setValue(key, value)` | Sets a property and returns success status. |
| `setValueOrThrow(key, value)`| Sets a property or throws an exception. |
| `setValueIfAbsent(key, value)` | Only sets if the property doesn't exist. |
| `setValueIf(key, value, pred)` | Only sets if the predicate matches the current value. |
| `setAllValues(map)` | Sets multiple properties. |
| `updateValue(key, transform)` | Updates a property based on its current value. |

### Removal

| Method | Description |
|--------|-------------|
| `removeValue(key)` | Removes a property and returns success status. |
| `removeValueOrThrow(key)` | Removes a property or throws an exception. |

## Platform Limitation

> **Warning**
>
> System properties management via `PropertiesScope` is currently only available on **JVM** and **Android** targets. On other platforms, these APIs are not exposed or functional.

## See Also

- [Environment Variables](System-Environment.md)
- [System Value Provider](../System-Value-Provider.md)
- [Platform Detection](../platform/System-Platform-Detection.md)
