# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.0.0

### Added

- **Platform Detection**: Type-safe detection for OS (Linux, macOS, Windows, Android, iOS, tvOS, watchOS) and environments (JVM, JS, Wasm).
- **UUID Utilities**: Full implementation for UUID Version 4 (random) and Version 7 (time-ordered) across all Kotlin targets.
- **Common Annotations**: Ecosystem-wide annotations including `@KoreDsl`, `@InternalKoreApi`, and `@ExperimentalKoreApi`.
- **Multiplatform Foundation**: Comprehensive target support including Mobile, Desktop, Web, and Native.
- **Security & Quality**: Integrated Detekt for static analysis and Trivy for vulnerability and license scanning.
- **Documentation**: Initial documentation setup using Writerside.

### Changed
- Initial stable release. Transitioned from internal development to a public library.
