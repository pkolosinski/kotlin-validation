package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid

typealias Validator<T> = (T) -> ValidationResult<T>

fun <T> buildValidator(
    failFast: Boolean = false,
    validationBlock: ValidationBuilder<T>.() -> Unit,
): Validator<T> = ValidationBuilder<T>(failFast = failFast)
    .apply(validationBlock)
    .build()

/**
 * Validates the [value] against the provided rules in [validationBlock].
 * @param failFast if `true`, stops after the first validation error; otherwise collects all errors.
 * @return a [ValidationResult] containing either the valid value or a list of validation errors.
 */
fun <T> validate(
    value: T,
    failFast: Boolean = false,
    validationBlock: ValidationBuilder<T>.() -> Unit,
): ValidationResult<T> = buildValidator(failFast = failFast, validationBlock)(value)

infix fun <T> Validator<T>.and(other: Validator<T>): Validator<T> = { value ->
    this(value) combine other(value)
}

fun <T> allOf(vararg validators: Validator<T>): Validator<T> = { value ->
    validators.map { it(value) }
        .fold(Valid(value) as ValidationResult<T>) { current, next ->
            current combine next
        }
}

private infix fun <T> ValidationResult<T>.combine(other: ValidationResult<T>): ValidationResult<T> =
    when (this) {
        is Valid if other is Valid -> this
        is Invalid if other is Invalid -> Invalid(this.errors + other.errors)
        is Invalid -> this
        else -> other
    }
