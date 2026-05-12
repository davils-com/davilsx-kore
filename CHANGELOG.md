# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
