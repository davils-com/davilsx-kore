# Annotations

The Kore library provides several annotations to control the scope, visibility, and usage of various components. These annotations help maintain a clean API and guide developers on how to use certain features.

## Available Annotations

- **[`@KoreDsl`](dsl.md)**: Marks a class or type as part of a Domain-Specific Language (DSL) within the Kore framework.
- **[`@KoreExperimental`](experimental.md)**: Indicates that an API is experimental and subject to change in future versions.
- **[`@KoreInternal`](internal.md)**: Marks an API as internal to the Kore library, meaning it should not be used in external code.

## Why use these?

Using these annotations ensures that:
- DSL elements don't leak into unintended scopes.
- Users are warned when using unstable features.
- Internal implementation details are protected from accidental usage.