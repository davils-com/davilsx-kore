# KoreDsl

The `@KoreDsl` annotation is used to create typesafe DSLs within the Kore library. It utilizes Kotlin's `@DslMarker` to prevent calling members of multiple DSL scopes at the same time.

## Usage

Apply the `@KoreDsl` annotation to classes that act as DSL builders or types that define a DSL context.

```kotlin
import com.davils.kore.annotation.KoreDsl

@KoreDsl
class MyDslBuilder {
    fun config(name: String) {
        // ...
    }
}

fun myDsl(init: MyDslBuilder.() -> Unit) {
    MyDslBuilder().apply(init)
}
```

## Why it's needed

Without `@DslMarker` (which `@KoreDsl` implements), nested DSL calls could access properties of their outer scope, leading to confusing and error-prone code.

### Example: Scope Protection

```kotlin
@KoreDsl
class OuterBuilder {
    fun outerAction() {}
}

@KoreDsl
class InnerBuilder {
    fun innerAction() {}
}

fun outer(block: OuterBuilder.() -> Unit) {}
fun OuterBuilder.inner(block: InnerBuilder.() -> Unit) {}

// Usage
outer {
    outerAction()
    inner {
        innerAction()
        // outerAction() // This would be a compilation error because of @KoreDsl!
    }
}
```

By using `@KoreDsl`, you ensure that the DSL remains clean and only relevant methods are available in each scope.