package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the property value is not null.
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Any> KProperty1<T, R?>.isNotNull(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it != null }
}

/**
 * Validates that the property value is null.
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Any> KProperty1<T, R?>.isNull(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null }
}
