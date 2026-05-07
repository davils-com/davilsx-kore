# Getting Started

Learn how to integrate Kore into your project and start using its core features.

## Installation

Kore is published to Maven Central. Add the dependency to your Kotlin Multiplatform project's `commonMain` source set.

### Gradle (Kotlin DSL)

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.davils:davilsx-kore:1.0.0")
            }
        }
    }
}
```

## Quick Start

Most of Kore's features are available globally or through simple entry points.

### Detect Platform

```kotlin
import com.davils.kore.system.platform.Platform

fun main() {
    if (Platform.isMacos) {
        println("Running on macOS")
    }
    
    println("Current OS: ${Platform.current.name}")
}
```

### Generate UUIDs

```kotlin
import com.davils.kore.uuid.Uuid

val randomId = Uuid.randomUuidV4()
val timeOrderedId = Uuid.randomUuidV7()

println("V4: $randomId")
println("V7: $timeOrderedId")
```

### Use JVM Properties (JVM Only)

```kotlin
import com.davils.kore.system.properties

properties {
    set("my.custom.property", "hello-world")
    val value = getValueOrNull("my.custom.property")
    println(value)
}
```