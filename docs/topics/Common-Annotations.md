# Common Annotations

Kore provides a set of common annotations and markers used across the DavilsX ecosystem to enforce architectural boundaries, DSL consistency, and API stability.

## DSL Markers

### @KoreDsl

The `@KoreDsl` annotation is used to mark classes and functions that are part of a Domain-Specific Language (DSL) within Kore or other DavilsX libraries. It prevents "receiver leakage" by enforcing that only the innermost receiver's members are accessible without an explicit `this`.

```kotlin
@KoreDsl
public class MyScope {
    // DSL members
}
```

## API Visibility & Stability

Kore uses Kotlin's "Opt-in Requirement" system to communicate the stability and intended usage of its APIs.

### @InternalKoreApi

APIs marked with `@InternalKoreApi` are intended for use only within the DavilsX ecosystem. They are public for technical reasons (e.g., to be accessible across different modules) but should not be used by end-users. These APIs can change at any time without notice.

### @ExperimentalKoreApi

APIs marked with `@ExperimentalKoreApi` are new and potentially unstable. They are provided for early feedback but may undergo breaking changes in future releases. Use them with caution.

## Usage

To use an API marked with these annotations, you must explicitly opt-in:

```kotlin
@OptIn(ExperimentalKoreApi::class)
fun useExperimentalFeature() {
    // ...
}
```

Alternatively, you can configure the Kotlin compiler to opt-in for an entire module in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets.all {
        languageSettings.optIn("com.davils.kore.annotation.ExperimentalKoreApi")
    }
}
```
