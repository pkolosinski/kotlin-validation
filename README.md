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
