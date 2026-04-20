# KoreExperimental

The `@KoreExperimental` annotation identifies features or APIs that are still in an experimental stage. These APIs are subject to change, replacement, or removal in future releases without prior notice.

## Why use `@KoreExperimental`?

1.  **Feedback**: We want to release features early to get community feedback.
2.  **Safety**: It serves as a warning to developers that the API is not yet stable.
3.  **Flexibility**: Allows us to refine the implementation based on real-world usage.

## Usage

When you use an API marked with `@KoreExperimental`, the Kotlin compiler will issue a warning unless you explicitly opt-in.

### Applying the Annotation

As a developer of the library, you apply it to classes, functions, or properties:

```kotlin
import com.davils.kore.annotation.KoreExperimental

@KoreExperimental
fun newExcitingFeature() {
    // ...
}
```

### Consuming Experimental APIs

If you want to use an experimental feature, you need to opt-in to it:

#### Option 1: At usage site
```kotlin
@OptIn(KoreExperimental::class)
fun useFeature() {
    newExcitingFeature()
}
```

#### Option 2: Entire file
```kotlin
@file:OptIn(KoreExperimental::class)
```

#### Option 3: Gradle configuration
You can opt-in globally for your project in `build.gradle.kts`:
```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=com.davils.kore.annotation.KoreExperimental")
    }
}
```

## Important Note

Avoid using experimental features in critical production code where long-term stability is required, as the next version might require manual changes to your codebase.