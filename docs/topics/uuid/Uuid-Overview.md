# UUID Utilities

Kore provides a full UUID implementation with native performance on all supported targets. It supports both standard random-based identifiers (v4) and time-ordered identifiers (v7).

## Why Kore UUID?

While many platforms have built-in UUID support, their APIs differ significantly. Kore provides a single, consistent API that works identically on JVM, iOS, Android, Web, and Native targets without any platform-specific code.

## Available Versions

### UUID v4 (Random)

Suitable for most general-purpose use cases where a unique identifier is needed.

```kotlin
import com.davils.kore.uuid.Uuid

val id = Uuid.randomUuidV4()
println(id) // e.g., "f47ac10b-58cc-4372-a567-0e02b2c3d479"
```

### UUID v7 (Time-Ordered)

UUID v7 is a newer standard that includes a millisecond-precision timestamp. This makes it monotonically increasing, which is significantly more efficient for database primary keys as it maintains index locality.

```kotlin
import com.davils.kore.uuid.Uuid

val orderedId = Uuid.randomUuidV7()
println(orderedId) // e.g., "017f22e2-79b0-7cc3-98c4-dc0c0c07398f"
```

## Parsing and Serialization

You can easily parse UUID strings back into `Uuid` objects.

```kotlin
import com.davils.kore.uuid.UuidV4
import com.davils.kore.uuid.toUuidV4

val id = "f47ac10b-58cc-4372-a567-0e02b2c3d479".toUuidV4()
```

## Performance

Kore's UUID implementation is optimized for each target:
- **JVM**: Uses `java.util.UUID` internally where appropriate but provides the common wrapper.
- **Native**: Uses high-quality random number generators provided by the OS.
- **Web**: Uses `crypto.getRandomValues()` for cryptographic security.
