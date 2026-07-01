# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.1.1

### Added
- **Event Bus Lifecycle**: `EventBus` and every contained `EventTopic` now implement the `Disposable` interface, ensuring that event bus resources can be properly released.
- **Topic-Only Event Bus (Kafka-Style, Breaking)**: The event bus is now strictly topic-based; there is no flat, single-stream API anymore. Every publication and every subscription targets a named [EventTopic].
    - `EventTopic<T>` handle carrying `push`, `emit`, `tryPush`, `pushAll`, `emitAll`, `tryPushAll`, `pushIf`, `emitIf`, `emitDelayed`, `events`, `subscribe`, and `dispose`, plus its declared `eventType` (`KClass<T>`).
    - `EventBus` container implementing `Disposable`, which disposes every declared topic atomically and exposes type-safe lookup via `bus.topic<T>("name")` guarded by a runtime `KClass` check.
    - `eventBus { topic<T>("name") { ... } }` DSL that also returns each declared `EventTopic<T>` directly to the caller, requiring at least one topic and offering per-topic replay/buffer/overflow/error configuration.
    - Bus-wide topic defaults on `EventBusBuilder` (`replay`, `extraBufferCapacity`, `overflowStrategy`, `onError`) that are inherited by every declared topic and can be individually overridden inside each `topic { ... }` DSL block.
    - `EventBusBuilder.produce()` now returns a pure `EventBusData` snapshot instead of an `EventBus` instance, following the DSL convention that builders emit data only; the `eventBus(...)` DSL entry point wraps that data into the actual `EventBus`.
- **Command-Line Argument Parsing**: Introduced `ProgramArguments` for thread-safe parsing and access to command-line arguments (supports `--` prefix and key-value pairs).
- **Application Environments**: Added `ApplicationMode` enum to manage different environments (DEV, STAGE, PROD, etc.) with built-in serialization and validation support.

### Changed
- **Kotlin Upgrade**: Updated Kotlin to version 2.4.0.
- **Gradle Upgrade**: Bumped Gradle Wrapper from 9.5.1 to 9.6.1.

### Removed
- **Removed (Breaking)**: The pre-existing flat `EventBus<E>` and its top-level `eventBus(...)` factory, together with `EventBusData`'s flat configuration and the `pushIf` / `emitIf` operators on the bus itself, have been removed in favor of the topic-based API.
- **Cleaned Workspace**: Removed unnecessary `yarn.lock` files from Kotlin JS and Wasm directories.

## 1.1.0

### Added
- **Resource Management (Loan Pattern)**: Introduced `Disposable` and `DisposableAsync` interfaces to support explicit resource lifecycle management and non-blocking cleanup.
- **Project Consistency**: Added `.editorconfig` to enforce unified coding styles and formatting across the codebase.

### Changed
- **Package Reorganization**: Improved project structure by regrouping modules into more specific categories:
    - DSL Pattern moved to `com.davils.kore.pattern.creational.dsl`.
    - Event Pattern moved to `com.davils.kore.pattern.reactive.event`.
    - System Environment moved to `com.davils.kore.system.environment`.
- **Java Compatibility**: Downgraded Java and JDK version from 25 to 17 in the build configuration to ensure better stability and compatibility with current tooling.
- **Dependency Updates**:
    - Bumped `com.google.devtools.ksp` to 2.3.9.
    - Bumped Gradle Wrapper from 9.5.0 to 9.5.1.

## 1.0.1

### Added
- **DSL Pattern Framework**: Introduced a comprehensive framework for building type-safe DSLs with validation and verification support.
    - `DslMarker` and `DslResult` interfaces for consistent builder patterns.
    - `Validator` and `DslValidator` for declarative data validation.
    - `DslVerification` system for complex business rule verification with custom failure reporting.
- **Event Bus Pattern**: Added a multiplatform `EventBus` implementation for decoupled component communication.
    - Support for synchronous and asynchronous event dispatching.
    - Metadata and correlation ID support for event tracing.
    - Scoped event handling with `EventMarker`.
- **Writerside Documentation**: Expanded documentation with detailed guides for Annotations and Patterns.
- Added `DslMarker`, `DslResult`, and `Dsl` interfaces to the `com.davils.kore.pattern.dsl` package.
- Introduced `Validator` and `DslValidator` for DSL validation.
- Added `DslVerification` and `DslVerificationFailure` for builder verification.
- Implemented `EventBus` and related components in `com.davils.kore.pattern.event`.

## 1.0.0

### Added

- **Platform Detection**: Type-safe detection for OS (Linux, macOS, Windows, Android, iOS, tvOS, watchOS) and environments (JVM, JS, Wasm).
- **JVM Native Bridge**: Thread-safe management for loading native libraries via `NativeInterface`.
- **System Properties DSL**: Advanced API and DSL for managing JVM system properties with `PropertiesScope`.
- **UUID Utilities**: Full implementation for UUID Version 4 (random) and Version 7 (time-ordered) across all Kotlin targets.
- **Common Annotations**: Ecosystem-wide annotations including `@KoreDsl`, `@InternalKoreApi`, and `@ExperimentalKoreApi`.
- **Multiplatform Foundation**: Comprehensive target support including Mobile, Desktop, Web, and Native.
- **Security & Quality**: Integrated Detekt for static analysis and Trivy for vulnerability and license scanning.
- **Documentation**: Initial documentation setup using Writerside.

### Changed
- Initial stable release. Transitioned from internal development to a public library.
