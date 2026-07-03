<p align="center">
  <img src="docs/images/kore.svg" alt="Kore Logo" width="250">
</p>

<h1 align="center">Kore</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0">
    <img src="https://img.shields.io/badge/License-Apache_2.0-Redtronics?style=for-the-badge&logo=apache&labelColor=white&color=blue" alt="License">
  </a>
  <a href="https://kotlinlang.org">
    <img src="https://img.shields.io/badge/Kotlin-2.3.21-Redtronics?style=for-the-badge&logo=kotlin&labelColor=white&color=purple" alt="Kotlin">
  </a>
  <a href="https://gradle.org">
    <img src="https://img.shields.io/badge/Gradle-9.5.0-Redtronics?style=for-the-badge&logo=gradle&labelColor=white&color=02303A" alt="Gradle">
  </a>
</p>

<p align="center">
  <strong>Kore</strong> is the core library of the DavilsX ecosystem, providing essential utilities and abstractions for Kotlin Multiplatform projects.
</p>

---

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Quick Start](#quick-start)
- [Documentation](#documentation)
- [Third-Party Software](#third-party-software)
- [Contributing](#contributing)
- [License & Ethics](#license--ethics)

---

## Overview

Building Kotlin Multiplatform (KMP) projects requires a solid foundation. **Kore** provides this foundation by:

*   **Standardizing Utilities**: Common abstractions for UUIDs, platform detection, and value providers.
*   **Enforcing Ecosystem Consistency**: Shared DSL markers and internal annotations used across DavilsX libraries.
*   **Cross-Platform Reliability**: Consistent behavior across JVM, Android, iOS, macOS, Windows, Linux, Wasm, and JS.
*   **Developer-First Design**: Type-safe APIs with explicit API mode enabled for maximum clarity.

---

## Core Features

### Platform Detection
Type-safe detection of the underlying system to handle platform-specific logic:
- **OS & Architecture**: Detect Linux, macOS, Windows, Android, and iOS.
- **Environment Context**: Identify if the code is running in a browser, JVM, or native environment.
- **Type-Safe API**: No more magic strings for platform checks.

### Concurrent Collections
Thread-safe, high-performance collections for multiplatform projects:
- **ConcurrentHashMap**: A `Mutex`-backed concurrent map implementation.
- **Atomic Operations**: Support for `compute`, `merge`, and other atomic transformations.
- **Coroutines Ready**: Built from the ground up to work with Kotlin Coroutines.

### Functional Patterns
Rust-inspired functional primitives for safer and more expressive code:
- **Option<T>**: A type-safe alternative to nullable types.
- **Functional Operators**: `map`, `flatMap`, `filter`, `fold`, and more.
- **Safety First**: Eliminate `NullPointerException` through explicit presence/absence handling.

### UUID Utilities
Full implementation of universally unique identifiers:
- **UUID V4**: Standard random-based identifiers.
- **UUID V7**: Time-ordered identifiers for better database performance.
- **Multiplatform Support**: Native performance on all supported Kotlin targets.

### Common Annotations
Enforces architectural boundaries and API stability:
- **DSL Markers**: Centralized `@KoreDsl` for consistent DSL development.
- **API Visibility**: `@InternalKoreApi` and `@ExperimentalKoreApi` for clear contract communication.
- **Compiler Support**: Integrated with Kotlin's opt-in requirement system.

### Multiplatform Foundation
Optimized for the modern Kotlin Multiplatform stack:
- **Wide Target Support**: From mobile (iOS/Android) to web (Wasm/JS) and desktop (JVM/Native).
- **Concurrency Ready**: Built with coroutines and atomic operations in mind.
- **Small Footprint**: Minimal dependencies to keep your project lean.

---

## Documentation

Detailed documentation for Kore is available in the following locations:

- **[Wiki](https://davils-com.github.io/davilsx-kore/overview.html)**: The main entry point for guides and documentation.
- **[Examples](./src/commonTest/kotlin/com/davils/kore)**: Explore the test suite for reference implementations.

---

## Quick Start

### Installation

Add the dependency to your Kotlin Multiplatform project's `commonMain` source set:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.davils:davilsx-kore:<latest>")
            }
        }
    }
}
```

### Usage

Kore is designed to be used as a library. Most features are available out-of-the-box:

#### Platform & UUID
```kotlin
import com.davils.kore.uuid.Uuid
import com.davils.kore.system.platform.Platform

// Generate a time-ordered UUID V7
val id = Uuid.randomUuidV7()

// Detect current platform
val os = Platform.current
println("Running on: ${os.name}")
```

#### Concurrent HashMap
```kotlin
import com.davils.kore.collections.ConcurrentHashMap
import com.davils.kore.pattern.functional.Option

val map = ConcurrentHashMap<String, Int>()

// Atomic compute operation
map.compute("key") { current ->
    current.map { it + 1 }.or(Option.some(1))
}

// Safe access with Option
val value = map.get("key") // Returns Option<Int>
```

#### Option Type
```kotlin
import com.davils.kore.pattern.functional.Option
import com.davils.kore.pattern.functional.toOption

val maybeValue = "Hello".toOption()
val result = maybeValue
    .filter { it.startsWith("H") }
    .map { it.uppercase() }
    .getOrElse { "DEFAULT" }
```

---

## Third-Party Software

Kore leverages various open-source technologies. For a full list of libraries and licenses, please refer to the [Third-Party Software](./THIRDPARTY.md) document.

---

## Contributing

We welcome all contributions! To maintain quality, please note:

- **Documentation**: Changes to API or behavior must be documented.
- **Tests**: Ensure your changes are covered by tests.
- **Standards**: Follow the established Kotlin style and project conventions.

---

## License & Ethics

- **License**: Published under the **Apache License 2.0**. See `LICENSE` for details.
- **Code of Conduct**: We adhere to our [Code of Conduct](CODE_OF_CONDUCT.md).

---

<p align="center">
  Maintained by <a href="https://github.com/davils-com"><b>Davils</b></a>
</p>
