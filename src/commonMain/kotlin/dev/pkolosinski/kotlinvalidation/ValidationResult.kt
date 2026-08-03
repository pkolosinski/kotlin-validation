package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid

public sealed interface ValidationResult<out T> {
    public data class Valid<T>(val value: T) : ValidationResult<T>

    public data class Invalid(val errors: List<ValidationError>) : ValidationResult<Nothing>

    public fun isValid(): Boolean = this is Valid

    public fun isInvalid(): Boolean = this is Invalid

    public fun getOrNull(): T? = when (this) {
        is Valid -> value
        is Invalid -> null
    }

    public fun errorsOrEmpty(): List<ValidationError> = when (this) {
        is Valid -> emptyList()
        is Invalid -> errors
    }

    public fun <R> fold(
        ifValid: (T) -> R,
        ifInvalid: (List<ValidationError>) -> R,
    ): R = when (this) {
        is Valid -> ifValid(value)
        is Invalid -> ifInvalid(errors)
    }
}
