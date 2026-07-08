package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid

sealed interface ValidationResult<out T> {
    data class Valid<T>(val value: T) : ValidationResult<T>

    data class Invalid(val errors: List<ValidationError>) : ValidationResult<Nothing>

    fun isValid(): Boolean = this is Valid

    fun isInvalid(): Boolean = this is Invalid

    fun getOrNull(): T? = when (this) {
        is Valid -> value
        is Invalid -> null
    }

    fun errorsOrEmpty(): List<ValidationError> = when (this) {
        is Valid -> emptyList()
        is Invalid -> errors
    }

    fun <R> fold(
        ifValid: (T) -> R,
        ifInvalid: (List<ValidationError>) -> R,
    ): R = when (this) {
        is Valid -> ifValid(value)
        is Invalid -> ifInvalid(errors)
    }
}

inline fun <T, R> ValidationResult<T>.map(transform: (T) -> R): ValidationResult<R> = when (this) {
    is Valid -> Valid(transform(value))
    is Invalid -> this
}

inline fun <T> ValidationResult<T>.mapErrors(
    transform: (List<ValidationError>) -> List<ValidationError>,
): ValidationResult<T> = when (this) {
    is Valid -> this
    is Invalid -> this.copy(errors = transform(errors))
}

inline fun <T> ValidationResult<T>.onValid(action: (T) -> Unit): ValidationResult<T> {
    if (this is Valid) action(value)
    return this
}

inline fun <T> ValidationResult<T>.onInvalid(
    action: (List<ValidationError>) -> Unit,
): ValidationResult<T> {
    if (this is Invalid) action(errors)
    return this
}
