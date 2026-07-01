# DSL Pattern

Kore provides a comprehensive framework for building type-safe, validated Domain-Specific Languages (DSLs) in Kotlin.

## Core Interfaces

### DslMarker
The base interface for all DSL components. It provides a common root for DSL marker annotations to prevent scope leakage.

### Dsl<T>
An interface for DSL builders that produce a result of type `T`. It defines the `produce()` method as the standard way to finalize a builder.

### DslValidator
A base class that combines DSL building with automated validation. It ensures that the data produced by the DSL meets all defined constraints before being returned.

## Example: Building a Validated DSL

### 1. Define the Data
Implement `DslVerifiableData` to provide validation logic.

```kotlin
import com.davils.kore.pattern.creational.dsl.verification.DslVerifiableData
import com.davils.kore.pattern.creational.dsl.validation.Validator

data class UserData(val name: String, val age: Int) : DslVerifiableData {
    override fun validate() = Validator.result {
        check(name.isNotEmpty()) { "Name must not be empty" }
        check(age >= 0) { "Age must be non-negative" }
    }
}
```

### 2. Create the Builder
Extend `DslValidator` to manage the DSL state and validation.

```kotlin
import com.davils.kore.pattern.creational.dsl.validation.DslValidator

class UserBuilder : DslValidator<UserData>() {
    var name: String = ""
    var age: Int = 0

    override fun data() = UserData(name, age)
}
```

### 3. Use the DSL
Create a factory function that uses the builder.

```kotlin
fun createUser(block: UserBuilder.() -> Unit): UserData {
    val builder = UserBuilder()
    builder.block()
    return builder.produce() // Throws DslVerificationException if invalid
}
```

## Benefits

- **Type Safety**: Leverage Kotlin's type system to ensure correct DSL usage.
- **Fail-Fast Validation**: Errors are caught immediately when `produce()` is called.
- **Consistency**: Standardized interfaces for all builders across the ecosystem.
