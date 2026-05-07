# Platform Detection

Kore provides a type-safe way to detect the underlying operating system and environment. This avoids the use of "magic strings" and provides a consistent API across all Kotlin targets.

## The Platform Object

The main entry point is the `com.davils.kore.system.platform.Platform` object. It provides boolean properties for quick checks and access to the current `Os` enum.

### Operating System Checks

You can check for specific operating systems using the following properties:

*   `Platform.isWindows`
*   `Platform.isLinux`
*   `Platform.isMacos`
*   `Platform.isIos`
*   `Platform.isAndroid`
*   `Platform.isTvOs`
*   `Platform.isWatchOs`

### Grouped Checks

For convenience, some platforms are grouped:

*   `Platform.isApple`: Returns `true` for macOS, iOS, tvOS, and watchOS.
*   `Platform.isUnix`: Returns `true` for Linux and all Apple platforms.
*   `Platform.isMobile`: Returns `true` for iOS and Android.

### Environment Checks

You can also detect the runtime environment:

*   `Platform.isJvm`: Returns `true` if running on a Java Virtual Machine (including Android).
*   `Platform.isWeb`: Returns `true` if running in a Browser (either JS or Wasm).

## The Os Enum

If you need to perform more complex logic or use a `when` expression, you can access `Platform.current`, which returns an `Os` enum value.

```kotlin
import com.davils.kore.system.platform.Platform
import com.davils.kore.system.platform.Os

val message = when (Platform.current) {
    Os.WINDOWS -> "Welcome to Windows"
    Os.MACOS -> "Hello from Mac"
    Os.LINUX -> "Greetings, Linux user"
    Os.ANDROID, Os.IOS -> "Hello from mobile"
    else -> "Running on ${Platform.current.name}"
}
```

## Internal Detection

Under the hood, Kore uses `expect/actual` declarations with the `OsDetector` object to provide native performance and accuracy for each target platform.
