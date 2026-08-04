package dev.pkolosinski.kotlinvalidation.fixtures

import dev.pkolosinski.kotlinvalidation.ValidationError
import dev.pkolosinski.kotlinvalidation.ValidationResult

val validResult = ValidationResult.Valid("value")

val invalidResult = ValidationResult.Invalid(
    listOf(
        ValidationError(
            path = "path",
            errorCode = "error_code",
            message = "error message",
        ),
    ),
)
