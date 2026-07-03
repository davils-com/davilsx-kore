# Option

The `Option<T>` type is an idiomatic Kotlin implementation of the optional type, heavily inspired by Rust's `Option` enum. It represents an optional value: every `Option` is either `Some` and contains a value, or `None`, and does not.

## Why use Option?

In Kotlin, we usually use nullable types (`T?`) to represent the absence of a value. While powerful, nullable types can sometimes lead to:
- Complex nested null checks.
- Ambiguity (is `null` a valid value or does it mean "missing"?).
- Lack of functional combinators in some contexts.

`Option<T>` provides a clear, functional API for working with optional values, making your code more expressive and safer.

## Basic Usage

### Creating an Option

```kotlin
import com.davils.kore.pattern.functional.Option
import com.davils.kore.pattern.functional.toOption

// From a nullable value
val some = Option.fromNullable("Hello")
val none = Option.fromNullable<String>(null)

// Direct creation
val someDirect = Option.some(42)
val noneDirect = Option.none<Int>()

// Using extension function
val extensionSome = "World".toOption()
val extensionNone = (null as String?).toOption()
```

### Checking Presence

```kotlin
val option = Option.some(10)

if (option.isSome()) {
    println("Value exists!")
}

if (option.isNone()) {
    println("Value is missing.")
}

// Rust-like predicates
val isEven = option.isSomeAnd { it % 2 == 0 }
```

## Functional Transformations

`Option` shines when used with its functional operators.

### Map and FlatMap

```kotlin
val result = Option.some(5)
    .map { it * 2 }           // Some(10)
    .flatMap { Option.some(it.toString()) } // Some("10")
```

### Filtering

```kotlin
val filtered = Option.some(10)
    .filter { it > 5 }        // Some(10)

val empty = Option.some(3)
    .filter { it > 5 }        // None
```

### Folding and Defaults

```kotlin
val value = Option.some(10)
val name = value.fold(
    ifSome = { "Value: $it" },
    ifNone = { "No value" }
)

val orElse = Option.none<Int>().getOrElse { 0 }
```

## Side Effects

Use `onSome` and `onNone` for side effects without changing the option.

```kotlin
Option.some("Success")
    .onSome { println("Got: $it") }
    .onNone { println("Missing!") }
```
