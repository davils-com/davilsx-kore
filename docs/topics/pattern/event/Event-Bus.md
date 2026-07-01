# Event Bus

The Kore Event Bus is a multi-topic, Kafka-inspired reactive communication system. It allows decoupled components to exchange events through strongly typed, named channels.

## Core Concepts

### EventTopic
An `EventTopic<T>` is a dedicated channel for a specific event type `T`. Every publication and subscription in Kore is strictly topic-based. Topics are declared upfront and are immutable for the lifetime of the bus.

### Strong Typing
Unlike traditional event buses that use a single flat stream, Kore enforces type safety. When you retrieve a topic, you must specify its event type, which is validated at runtime against the declaration.

## Basic Usage

### 1. Define your Events
All events must implement the `EventMarker` interface.

```kotlin
import com.davils.kore.pattern.reactive.event.EventMarker

data class UserLogged(val userId: String) : EventMarker
```

### 2. Configure the Event Bus
Use the `eventBus` DSL to declare your topics.

```kotlin
import com.davils.kore.pattern.reactive.event.eventBus

val bus = eventBus(scope) {
    topic<UserLogged>("auth") {
        replay = 1
    }
}
```

### 3. Publish and Subscribe
Retrieve the topic handle and interact with it.

```kotlin
val authTopic = bus.topic<UserLogged>("auth")

// Subscribe to events
authTopic.subscribe { event ->
    println("User logged in: ${event.userId}")
}

// Publish an event
authTopic.push(UserLogged("user-123"))
```

## Advanced Configuration

You can set bus-wide defaults that are inherited by every topic, or override them individually.

```kotlin
val bus = eventBus(scope) {
    // Global defaults
    replay = 0
    extraBufferCapacity = 64
    
    topic<SystemEvent>("system") // Uses defaults
    
    topic<AlertEvent>("alerts") {
        // Override for this topic
        replay = 10
        extraBufferCapacity = 128
    }
}
```

## Lifecycle Management

The `EventBus` and `EventTopic` implement the `Disposable` interface. When you call `bus.dispose()`, it atomically:
1. Cancels all active subscriptions.
2. Closes all topic flows.
3. Rejects any further publication attempts.
