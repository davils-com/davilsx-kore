# Kore Internal

<primary-label ref="annotation"/>

The `@KoreInternal` annotation marks an API as internal to the Kore library. These APIs are public for technical reasons (e.g., cross-module access) but are not intended for public consumption.

## Purpose

Internal APIs often include implementation details, low-level utilities, or architectural components that are essential for the library's internal workings but are too unstable or specific for general use.

## Opt-in Requirement (Error Level)

Unlike experimental APIs, `@KoreInternal` uses `RequiresOptIn.Level.ERROR`. This means the compiler will treat any unauthorized usage as a compilation error unless you explicitly opt-in.

### Why you should avoid Internal APIs

- **No Stability Guarantee**: These APIs can change or be deleted in any version (even patches) without notice.
- **Implementation Detail**: They often expose logic that assumes a specific internal state of the library.
- **Not Documented**: Internal APIs are generally not part of the public documentation and lack the support guarantees of public APIs.

## Usage (Discouraged)

If you absolutely must use an internal API (e.g., when building a low-level extension for the Davils ecosystem), you can opt-in like this:

```kotlin
import com.davils.kore.annotation.KoreInternal

@OptIn(KoreInternal::class)
fun lowLevelExtension() {
    // Accessing internal Kore logic
    InternalKoreUtility.performAction()
}
```

## Module-wide Opt-in

For internal ecosystem projects, you can opt-in for the entire module:

```kotlin
kotlin {
    sourceSets.all {
        languageSettings.optIn("com.davils.kore.annotation.KoreInternal")
    }
}
```

> **Warning**
>
> Using internal APIs is highly discouraged for application developers. If you find yourself needing an internal API, please consider opening an issue to discuss making it public or providing a stable alternative.