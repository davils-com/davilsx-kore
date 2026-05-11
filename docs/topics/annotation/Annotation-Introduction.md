# Annotation Introduction

<primary-label ref="annotation"/>

Kore provides a set of common annotations and markers used across the ecosystem to enforce architectural boundaries, DSL consistency, and API stability.

## Overview

These annotations help developers understand the intended usage of different parts of the library:

| Annotation          | Purpose                                             | Stability    |
|---------------------|-----------------------------------------------------|--------------|
| `@KoreDsl`          | Marker for Domain-Specific Languages.               | Stable       |
| `@KoreExperimental` | Marks an API as experimental and subject to change. | Experimental |
| `@KoreInternal`     | Marks an API as internal to the library.            | Internal     |

## Why use Annotations?

Annotations in Kore serve several critical purposes:

- **Scope Control**: Prevent accidental usage of outer scopes in nested DSL structures using `@KoreDsl`.
- **API Evolution**: Introduce new features as `@KoreExperimental` to gather feedback before finalizing them.
- **Encapsulation**: Mark implementation details as `@KoreInternal` to maintain a clean public API.
- **Safety**: Ensure that developers are aware when they are using unstable or internal APIs.

## See Also

- [Kore DSL](Annotation-Kore-DSL.md)
- [Kore Experimental](Annotation-Kore-Experimental.md)
- [Kore Internal](Annotation-Kore-Internal.md)