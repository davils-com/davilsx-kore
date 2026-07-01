# Application Mode

The `ApplicationMode` enum provides a standardized way to define and manage the execution environment of your application (e.g., Development, Staging, Production).

## Overview

Instead of using raw strings or custom constants, Kore provides a type-safe enum with built-in support for common environments. This ensures consistency across your entire ecosystem.

## Available Modes

- `DEV`: Development environment.
- `TEST`: Testing environment.
- `STAGE`: Staging/QA environment.
- `PROD`: Production environment.
- `DEBUG`: Local debug environment.

## Usage

### Getting the Current Mode

You can determine the current mode from a string (e.g., from an environment variable or system property).

```kotlin
import com.davils.kore.system.environment.ApplicationMode

val mode = ApplicationMode.fromString("PROD")
println(mode) // ApplicationMode.PROD
```

### Checking the Mode

`ApplicationMode` provides helper properties for common checks.

```kotlin
if (mode.isDev) {
    // Enable verbose logging
}

if (mode.isProd) {
    // Enable security optimizations
}
```

## Serialization

`ApplicationMode` is compatible with `kotlinx.serialization`, making it easy to use in configuration files or network requests.
