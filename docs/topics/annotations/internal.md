# KoreInternal

The `@KoreInternal` annotation is used to mark APIs that are part of the internal implementation details of the Kore library. These APIs are not intended for public use and may change or be removed at any time without notice.

## Why `@KoreInternal`?

Some components must be public due to technical reasons (e.g., accessed from different modules or packages), but they are not intended for use by library consumers. `@KoreInternal` helps to:

1.  **Protect Implementation**: It prevents users from depending on implementation details that we want to keep flexible.
2.  **Clear API Surface**: It signals that these components are not part of the official public API.
3.  **Prevent Breaking Changes**: Since these are internal, we can change them without it being a breaking change for the public API.

## Usage

When an API is marked with `@KoreInternal`, the Kotlin compiler will treat its usage in other code as an **ERROR** unless you explicitly opt-in.

### Applying the Annotation

Internal components are marked like this:

```kotlin
import com.davils.kore.annotation.KoreInternal

@KoreInternal
class InternalLogic {
    fun execute() {
        // ...
    }
}
```

## Opting In (NOT Recommended)

While you can opt-in to use internal APIs, it is strongly discouraged as your code will likely break in the next version of Kore.

### Option 1: At usage site
```kotlin
@OptIn(KoreInternal::class)
fun useInternalLogic() {
    InternalLogic().execute()
}
```

### Option 2: Gradle configuration
```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=com.davils.kore.annotation.KoreInternal")
    }
}
```

## Summary

If you find yourself needing to use something marked with `@KoreInternal`, please check if there is a public alternative. Using internal APIs is a risk and can lead to unstable applications.