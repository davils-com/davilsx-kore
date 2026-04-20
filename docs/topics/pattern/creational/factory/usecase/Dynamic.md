# Dynamic Parameterized Factories

Parameterized factories are powerful for creating objects based on dynamic input. You can combine them with domain logic to create a flexible "Registry" or "Provider" system.

## Implementation

Define a `FactoryParameter` and implement the `FactoryParameterized` interface. You can use logic like `when` expressions to determine which specific object to create based on the parameter.

```kotlin
import com.davils.kore.pattern.creational.factory.FactoryParameter
import com.davils.kore.pattern.creational.factory.FactoryParameterized

data class PaymentConfig(val method: String, val amount: Double) : FactoryParameter

object PaymentProcessorFactory : FactoryParameterized<PaymentConfig, Processor> {
    override fun create(parameter: PaymentConfig): Processor {
        return when (parameter.method) {
            "CREDIT_CARD" -> CreditCardProcessor(parameter.amount)
            "PAYPAL" -> PayPalProcessor(parameter.amount)
            else -> throw IllegalArgumentException("Unknown payment method")
        }
    }
}
```

## Usage

Call the factory with the appropriate configuration parameter.

```kotlin
val processor = PaymentProcessorFactory.create(PaymentConfig("PAYPAL", 99.99))
```

---

## When to use this?

- **Service Registry**: When you have multiple implementations of a service and want a single entry point to create them based on a "type" or "key".
- **Dynamic Configuration**: When the behavior or configuration of the created object depends on external settings.
- **Plugin Systems**: To create specific plugin instances based on a configuration file or input.
