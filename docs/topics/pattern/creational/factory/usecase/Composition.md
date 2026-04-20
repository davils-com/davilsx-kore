# Composition with Factories

Because Kore factories are functional interfaces, you can compose them using the `map` and `mapAsync` extensions. This allows you to build complex creation pipelines by chaining transformations.

## Implementation

Chain multiple factories using the `map` extension to transform the output of one factory into another type.

```kotlin
import com.davils.kore.pattern.creational.factory.Factory

// Step 1: Create a factory that fetches raw data
val rawDataFactory = Factory { fetchRawData() }

// Step 2: Transform raw data into a domain model
val modelFactory = rawDataFactory.map { raw -> DomainModel.from(raw) }

// Step 3: Transform the model into a view state
val viewStateFactory = modelFactory.map { model -> ViewState.from(model) }
```

## Usage

Calling the final factory in the chain executes the entire creation pipeline.

```kotlin
// The final factory handles the entire pipeline
val state = viewStateFactory.create()
```

---

## When to use this?

- **Data Pipelines**: When you have multiple stages of data processing or transformation.
- **Layered Architecture**: To bridge the gap between different layers of your application (e.g., from Data Transfer Object (DTO) to Domain Model to View State).
- **Separation of Concerns**: When you want each factory to focus on a single transformation step.
