# Kotlin Validation

Simple library for validating data in Kotlin.

## Quick Start

Define a validator with a DSL-like syntax, then call it on your DTO:

```kotlin
data class User(
    val id: String?,
    val name: String,
    val email: String,
    val age: Int,
)

val userValidator: (User) -> ValidationResult<User> = { dto ->
    validate(dto) {
        ensureNotNull(
            dto.id?.let { Uuid.parseOrNull(it) },
            "ID must be a valid UUID",
        )
        ensureNotBlank(dto.name, "Name cannot be blank")
        ensure(dto.email.matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")), "Invalid email format")
        ensure(dto.age > 0, "Age must be positive")
    }
}

// Valid user
val result = userValidator(User("a1b2c3d4", "Alice", "alice@example.com", 30))
// → Valid(User(...))

// Invalid user
val result = userValidator(User("not-uuid", "", "bad-email", -1))
// → Invalid with 4 error messages
```

### API overview

| Function         | Purpose                                        |
|------------------|------------------------------------------------|
| `validate`       | Entry point — runs rules and returns a result. |
| `ensure`         | Fails if `condition` is false.                 |
| `ensureNotNull`  | Fails if the value is `null`.                  |
| `ensureNotBlank` | Fails if the string is `null` or blank.        |

All violations are collected and returned together (collect-all mode).

### Map validation

Map properties support whole-map predicates, size rules, and nested key/value validation:

```kotlin
data class Request(val attributes: Map<String, String?>?)

val validator = buildValidator {
    Request::attributes.isNotEmpty()
    Request::attributes.eachSatisfies(errorCode = "ATTRIBUTE_BLANK") {
        it.isNotBlank()
    }
    Request::attributes.validateKeys {
        ensure(errorCode = "ATTRIBUTE_KEY") { it.startsWith("x-") }
    }
    Request::attributes.validateEntries {
        Map.Entry<String, String?>::value.isNotNull(errorCode = "ATTRIBUTE_REQUIRED")
    }
}
```

Nested map errors use paths such as `attributes[sku]` and `attributes[sku].value`. Map-specific
size and key/value traversal helpers treat null maps as valid and skip null elements, while
whole-map `satisfies` controls null handling through its predicate and entry validation can check
nullable keys or values explicitly.
