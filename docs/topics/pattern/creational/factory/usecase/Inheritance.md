# Factory with Inheritance

For more complex scenarios, you can define a base factory class or interface and provide specialized implementations through inheritance. This allows you to encapsulate common logic in the base class while delegating specific creation details to subclasses.

## Implementation

Define an abstract class that implements the `Factory` interface. You can provide common properties or helper methods that all subclasses can use.

```kotlin
import com.davils.kore.pattern.creational.factory.Factory

abstract class BaseUserFactory : Factory<User> {
    protected abstract val role: String
    
    override fun create(): User {
        val id = generateId()
        return User(id = id, role = role)
    }
    
    private fun generateId(): String = "USR-${System.currentTimeMillis()}"
}

class AdminFactory : BaseUserFactory() {
    override val role: String = "ADMIN"
}

class GuestFactory : BaseUserFactory() {
    override val role: String = "GUEST"
}
```

## Usage

Instantiate the specific factory implementation you need.

```kotlin
val adminFactory = AdminFactory()
val admin = adminFactory.create()

val guestFactory = GuestFactory()
val guest = guestFactory.create()
```

---

## When to use this?

- **Shared Logic**: When multiple factories share the same creation steps but produce different variations of an object.
- **Template Method Pattern**: When you want to define the skeleton of an algorithm (creation process) and let subclasses redefine certain steps.
- **Domain Specialization**: When you have a generic entity (like `User`) but want distinct factories for different types (like `Admin`, `Guest`).
