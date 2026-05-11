# Kore DSL

<primary-label ref="annotation"/>

The `@KoreDsl` annotation is a marker used to define Domain-Specific Languages (DSLs) within the Kore library. It leverages Kotlin's `@DslMarker` to prevent "receiver leakage" in nested DSL structures.

## Purpose

When building nested DSLs, it is easy to accidentally call a function from an outer scope. `@KoreDsl` ensures that only the members of the immediate receiver are accessible, making the DSL safer and more intuitive to use.

## Usage

Apply `@KoreDsl` to your DSL configuration classes or types.

```kotlin
import com.davils.kore.annotation.KoreDsl

@KoreDsl
class ConfigurationBuilder {
    var name: String = ""
    
    fun child(block: ChildBuilder.() -> Unit) {
        // ...
    }
}

@KoreDsl
class ChildBuilder {
    var value: Int = 0
}
```

## Example: Scope Protection

Without `@DslMarker` (which `@KoreDsl` provides), the following code would be valid but potentially confusing:

```kotlin
configuration {
    name = "Parent"
    child {
        // This would be valid without @KoreDsl, 
        // but @KoreDsl prevents accessing 'name' here.
        name = "Accidental parent modification" 
        value = 10
    }
}
```

With `@KoreDsl`, the compiler will report an error if you try to access `name` inside the `child` block without an explicit receiver, as it belongs to the outer `ConfigurationBuilder` scope.

## Key Benefits

- **Improved Readability**: Clear boundaries for each level of the DSL.
- **Error Prevention**: Prevents accidental modification of parent scopes.
- **IDE Support**: Better auto-completion suggestions by filtering out invalid scope members.