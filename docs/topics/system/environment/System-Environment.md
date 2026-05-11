# Environment Variables

<primary-label ref="annotation"/>

Kore provides a unified and type-safe way to access system environment variables across different platforms.

## Overview

The environment system is designed to be simple yet flexible, providing both a direct property-based access and a DSL-based approach for more complex scenarios.

### Key Features

- **Multiplatform Support**: Provides a consistent API while acknowledging platform-specific limitations.
- **DSL-powered**: Easily retrieve, validate, and transform environment variables.
- **Safety**: Built-in support for platform capability checks and mandatory value enforcement.

## Basic Usage

The simplest way to access an environment variable is via the `env` property or the `environment()` function.

```kotlin
import com.davils.kore.system.environment.env

val apiKey = env.getOrNull("API_KEY")
val port = env.getOrDefault("PORT", "8080")
```

## Environment DSL

For more advanced use cases, Kore provides an `environment` builder that opens a scope for various operations.

```kotlin
import com.davils.kore.system.environment.environment

val config = environment {
    val host = getValueOrDefault("HOST", "localhost")
    val port = getValueOrThrow("PORT").toInt()
    
    "Server configured at $host:$port"
}
```

### Scope Functions

Inside an `environment` block (or on the `env` object), you have access to several utility methods:

| Method | Description |
|--------|-------------|
| `getValueOrNull(key)` | Returns the value or `null` if not found. |
| `getValueOrDefault(key, default)` | Returns the value or a provided fallback. |
| `getValueOrThrow(key)` | Returns the value or throws an exception if missing. |
| `contains(key)` | Checks if an environment variable is set. |

## Platform Support

Environment variable support varies by platform. You can check if the current environment supports variable access using `Environment.isSupported`.

| Platform | Support |
|----------|---------|
| JVM      | Full support via `System.getenv()`. |
| Android  | Limited support. |
| Native   | Supported on most Unix/Windows targets. |
| JS       | Not supported (returns `null`). |
| WASM     | Not supported (returns `null`). |

### Handling Unsupported Platforms

When using the `environment` builder, Kore checks for platform support by default.

```kotlin
// Throws UnsupportedOperationException if platform is not supported
environment {
    val path = getValueOrNull("PATH")
}

// Returns null if platform is not supported
val result = environmentOrNull {
    getValueOrNull("PATH")
}

// Force execution even if platform doesn't officially support it
environment(ignorePlatformSupport = true) {
    // ...
}
```

## See Also

- [System Properties](../properties/System-Properties.md)
- [System Value Provider](../System-Value-Provider.md)
- [Platform Detection](../platform/System-Platform-Detection.md)
