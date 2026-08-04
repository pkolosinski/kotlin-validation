package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid

public inline fun <T, R> ValidationResult<T>.map(transform: (T) -> R): ValidationResult<R> =
    when (this) {
        is Valid -> Valid(transform(value))
        is Invalid -> this
    }

public inline fun <T> ValidationResult<T>.mapErrors(
    transform: (List<ValidationError>) -> List<ValidationError>,
): ValidationResult<T> = when (this) {
    is Valid -> this
    is Invalid -> this.copy(errors = transform(errors))
}

public inline fun <T> ValidationResult<T>.onValid(action: (T) -> Unit): ValidationResult<T> {
    if (this is Valid) action(value)
    return this
}

public inline fun <T> ValidationResult<T>.onInvalid(
    action: (List<ValidationError>) -> Unit,
): ValidationResult<T> {
    if (this is Invalid) action(errors)
    return this
}
