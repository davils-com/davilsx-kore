# Overview

**Kore** is the fundamental core library of the DavilsX ecosystem. It provides essential utilities, abstractions, and standardizations for Kotlin Multiplatform (KMP) projects, ensuring consistency across different platforms and modules.

## Key Principles

*   **Standardization**: Providing common abstractions for UUIDs, platform detection, and value providers.
*   **Consistency**: Shared DSL markers and internal annotations used across all DavilsX libraries.
*   **Reliability**: Consistent behavior across JVM, Android, iOS, macOS, Windows, Linux, Wasm, and JS.
*   **Developer-First**: Type-safe APIs with explicit API mode enabled for maximum clarity and safety.

## Core Components

### Platform Detection
Type-safe detection of the underlying operating system and environment to handle platform-specific logic without magic strings.

### UUID Utilities
Full implementation of Universally Unique Identifiers, including version 4 (random) and version 7 (time-ordered), optimized for performance on all targets.

### Common Annotations
Architectural boundaries and API stability markers, including centralized DSL markers and internal/experimental API requirements.

### JVM Native Bridge
(JVM only) Thread-safe management for loading native libraries, simplifying the integration of C/C++ or Rust code.

### System Properties DSL
(JVM only) An advanced, type-safe DSL for managing JVM system properties.

## Target Support

Kore supports a wide range of Kotlin targets:
- **Mobile**: Android, iOS
- **Desktop**: Windows (Mingw), macOS, Linux
- **Web**: JS (Browser), Wasm (Browser)
- **Other**: tvOS, watchOS
