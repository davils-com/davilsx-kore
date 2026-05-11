# Operating System (Os)

<primary-label ref="annotation"/>

The `Os` enum represents the various operating systems and platforms supported by Kore. It is the core type used for platform identification throughout the library.

## Supported Operating Systems

Kore explicitly supports the following operating systems:

| Enum Constant | String Value | Description |
|---------------|--------------|-------------|
| `WINDOWS`     | `windows`    | Microsoft Windows |
| `LINUX`       | `linux`      | Linux-based systems |
| `MACOS`       | `macos`      | Apple macOS |
| `IOS`         | `ios`        | Apple iOS |
| `ANDROID`     | `android`    | Google Android |
| `WASM`        | `webassembly`| WebAssembly environment |
| `JS`          | `javascript` | JavaScript environment |
| `TVOS`        | `tvos`       | Apple tvOS |
| `WATCHOS`     | `watchos`    | Apple watchOS |
| `UNKNOWN`     | `unknown`    | Fallback for unidentified systems |

## Features

### Serialization

The `Os` enum is `@Serializable`. It serializes to its `value` string property. This is particularly useful when storing platform information in configuration files or sending it over the network.

```kotlin
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.davils.kore.system.platform.Os

val jsonString = Json.encodeToString(Os.WINDOWS) 
// Result: "windows"
```

### Companion Utilities

The `Os` companion object provides several methods for creating instances from string values:

- **`byValueOrNull(value: String)`**: Returns the matching `Os` or `null`. Comparison is case-insensitive.
- **`byValueOrDefault(value: String, default: Os)`**: Returns the matching `Os` or a fallback (defaults to `UNKNOWN`).
- **`byValue(value: String)`**: Returns the matching `Os` or throws an `IllegalArgumentException`.

```kotlin
val myOs = Os.byValueOrNull("Linux") // Returns Os.LINUX
val unknown = Os.byValueOrDefault("Commodore64") // Returns Os.UNKNOWN
```

## Integration with Platform

While `Os` provides the raw type, the `Platform` object is typically used to retrieve the current OS:

```kotlin
val currentOs: Os = Platform.current
println("You are running on: ${currentOs.value}")
```

## Internal Detection (JVM)

On the JVM, the `OsDetector` uses keyword matching against the `os.name` system property:

- **Windows**: Matches "wind" or "winnt".
- **Linux/Unix**: Matches "nux", "sun", "bsd", "ubu", "cent", "deb".
- **macOS**: Matches "mac" or "dar" (Darwin).

## See Also

- [Platform Detection](System-Platform-Detection.md)
- [System Properties](../System-Value-Provider.md)
