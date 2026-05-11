# Platform Detection

<primary-label ref="annotation"/>

Kore provides a robust and type-safe platform detection system that allows you to identify the underlying operating system and runtime environment in a Kotlin Multiplatform project.

## Overview

The platform detection system is centered around the `Platform` object, which provides high-level properties to check for specific platforms or groups of platforms (e.g., mobile, Apple, Unix).

### Key Features

- **Multiplatform Support**: Works across all supported Kore targets (JVM, Native, JS, WASM).
- **Type-Safe**: Uses the `Os` enum to represent operating systems.
- **Granular Detection**: Distinguishes between specific OS versions and runtime environments (like JVM vs. Native).
- **Group Checks**: Easily identify categories like "Mobile" (Android + iOS) or "Apple" (macOS + iOS + tvOS + watchOS).

## The Platform Object

The `Platform` object is the primary entry point for platform detection. It provides a set of boolean properties for quick checks.

### Basic OS Checks

| Property    | Description                                          |
|-------------|------------------------------------------------------|
| `isWindows` | `true` if running on Microsoft Windows.              |
| `isLinux`   | `true` if running on Linux.                          |
| `isMacos`   | `true` if running on Apple macOS.                    |
| `isIos`     | `true` if running on Apple iOS.                      |
| `isAndroid` | `true` if running on Google Android.                 |
| `isTvOs`    | `true` if running on Apple tvOS.                     |
| `isWatchOs` | `true` if running on Apple watchOS.                  |
| `isWeb`     | `true` if running in a Web environment (JS or WASM). |

### Group & Runtime Checks

| Property   | Description                                                          |
|------------|----------------------------------------------------------------------|
| `isJvm`    | `true` if the runtime is a Java Virtual Machine.                     |
| `isMobile` | `true` if running on a mobile platform (Android or iOS).             |
| `isApple`  | `true` if running on any Apple platform (macOS, iOS, tvOS, watchOS). |
| `isUnix`   | `true` if running on a Unix-based system (Linux + Apple platforms).  |

## Usage Examples

### Platform-Specific Logic

You can use the `Platform` object to execute code only on specific platforms:

```kotlin
import com.davils.kore.system.platform.Platform

fun initializeFileSystem() {
    if (Platform.isWindows) {
        println("Initializing Windows-specific file system...")
    } else if (Platform.isUnix) {
        println("Initializing Unix-specific file system...")
    }
}
```

### Categorized Behavior

Simplify checks for mobile or desktop-specific features:

```kotlin
fun getUITheme() {
    if (Platform.isMobile) {
        // Apply mobile-optimized theme
    } else {
        // Apply desktop/web theme
    }
}
```

## How it Works

Internally, Kore uses an `expect/actual` object called `OsDetector` to fetch platform information at runtime.

- **JVM**: Detects the OS using the `os.name` system property.
- **Android**: Hardcoded to return `Os.ANDROID`.
- **Native**: Each target (e.g., `macosArm64`, `linuxX64`) has its own `actual` implementation returning the corresponding `Os` constant.
- **JS/WASM**: Returns `Os.JS` or `Os.WASM` respectively.

## See Also

- [Operating System Enum](System-Platform-Os.md)
- [System Value Provider](System-Value-Provider.md)
