# Kore Experimental

<primary-label ref="annotation"/>

The `@KoreExperimental` annotation marks an API as experimental within the Kore library. These APIs are functional but may undergo breaking changes, renamed, or even removed in future releases based on community feedback.

## Purpose

Experimental APIs allow the library to evolve rapidly by providing new features to users early, without the strict compatibility guarantees of a stable API.

## Opt-in Requirement

Using an API marked with `@KoreExperimental` requires an explicit opt-in. This ensures that you are aware of the experimental nature of the feature.

### Option 1: Single Usage Opt-in

You can opt-in for a specific call site or function using the `@OptIn` annotation.

```kotlin
import com.davils.kore.annotation.KoreExperimental

@OptIn(KoreExperimental::class)
fun myNewFeature() {
    // Calling an experimental Kore API
    experimentalKoreFunction()
}
```

### Option 2: Module-wide Opt-in

If you use experimental APIs extensively in your project, you can opt-in for the entire module in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets.all {
        languageSettings.optIn("com.davils.kore.annotation.KoreExperimental")
    }
}
```

## Warning Level

The `@KoreExperimental` annotation is configured with `RequiresOptIn.Level.WARNING`. This means the compiler will issue a warning if you use the API without opting in.

> **Important**
>
> While it is only a warning, we strongly recommend opting in explicitly to acknowledge that your code depends on an unstable API.