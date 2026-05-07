# UUID Utilities

Kore includes a robust implementation for Universally Unique Identifiers (UUID) that works seamlessly across all Kotlin Multiplatform targets.

## Supported Versions

Currently, Kore supports two versions of UUIDs:

*   **UUID V4**: Randomly generated identifiers. Best for general-purpose unique IDs where ordering is not important.
*   **UUID V7**: Time-ordered identifiers. Recommended for database primary keys as they are sortable by creation time, which improves indexing performance.

## Usage

### Generating UUIDs

You can generate new UUIDs using the `Uuid` companion object:

```kotlin
import com.davils.kore.uuid.Uuid

val v4 = Uuid.randomUuidV4()
val v7 = Uuid.randomUuidV7()
```

### Working with UUIDs

All UUID classes inherit from the abstract `Uuid` class, which provides standard functionality:

*   **Comparison**: UUIDs are `Comparable`, allowing them to be sorted.
*   **Equality**: Overridden `equals` and `hashCode` based on the UUID string value.
*   **String Representation**: Use `.toString()` or the `.value` property to get the standard hyphenated string format.
*   **Property Delegation**: UUIDs can be used as property delegates.

```kotlin
val id = Uuid.randomUuidV7()
val stringId: String by id // stringId will be the UUID string
```

## Why UUID V7?

Traditional UUID V4 identifiers are random, which causes "index fragmentation" in B-tree based databases when used as primary keys. New records are inserted at random locations in the index, leading to frequent page splits and poor performance.

**UUID V7** solves this by including a millisecond-precision timestamp in the first 48 bits. This makes them monotonically increasing (mostly), allowing databases to append new records to the end of the index, which is significantly more efficient.

## Validation

Kore automatically validates the format of UUIDs during construction. If you attempt to create a UUID from an invalid string, an `IllegalArgumentException` will be thrown.
