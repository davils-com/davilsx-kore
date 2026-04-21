# Code Design Patterns

Design patterns are typical solutions to common problems in software design. Each pattern is like a blueprint that you can customize to solve a particular design problem in your code.

The Kore library provides a set of highly optimized, functional-style implementations of common design patterns.

## Creational Patterns

Creational patterns provide various object creation mechanisms, which increase flexibility and reuse of existing code.

- **[](Standard-Factory.md)**: A functional interface for creating objects.
- **[](Async-Factory.md)**: A coroutine-based factory for async object creation.
- **[](Standard-Parameterized.md)**: A factory that takes input parameters for object creation.
- **[](Async-Parameterized.md)**: An async factory with input parameters.
- **[Standard Singleton](Standard-Singleton.md)**: The base interface for shared instances.
- **[Scoped Singleton](Scoped-Singleton.md)**: A thread-safe, lazily initialized singleton.
- **[Resettable Singleton](Resettable-Singleton.md)**: A singleton that can be cleared and re-initialized.
- **[Parameterized Singleton](Parameterized-Singleton.md)**: A singleton that provides an instance based on a parameter.