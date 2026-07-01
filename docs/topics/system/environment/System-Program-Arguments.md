# Program Arguments

Kore provides a thread-safe and easy-to-use utility for parsing and accessing command-line arguments across all platforms.

## Overview

The `ProgramArguments` class parses raw command-line arguments (from `main(args: Array<String>)`) into a structured format, supporting both flags (e.g., `--debug`) and key-value pairs (e.g., `--port=8080`).

## Usage

### 1. Parsing Arguments

Pass the raw arguments array to the `ProgramArguments` constructor.

```kotlin
import com.davils.kore.arguments.ProgramArguments

fun main(args: Array<String>) {
    val programArgs = ProgramArguments(args)
    // ...
}
```

### 2. Accessing Values

`ProgramArguments` provides several methods to retrieve argument values.

```kotlin
// Check for a flag
val isDebug = programArgs.hasArgument("debug")

// Get a value by key
val port = programArgs.getArgument("port") ?: "8080"

// Get all keys
val keys = programArgs.keys()
```

## Argument Format

The parser supports the following formats:

- **Flags**: `--debug` or `-verbose` (stored as keys with empty values, `hasArgument` returns true).
- **Key-Value**: `--port=8080` or `-env=prod`.
- **Positional**: `command` (stored as keys with empty values).

## Platform Support

Since `ProgramArguments` only relies on `commonMain` logic and standard Kotlin `Array<String>`, it is available on all supported platforms (JVM, Native, JS, WASM).
