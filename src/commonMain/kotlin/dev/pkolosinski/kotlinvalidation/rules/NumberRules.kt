package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the property value is positive (greater than zero).
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Number> KProperty1<T, R?>.isPositive(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.toDouble() > 0 }
}

/**
 * Validates that the property value is non-negative (greater than or equal to zero).
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Number> KProperty1<T, R?>.isNonNegative(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.toDouble() >= 0 }
}

/**
 * Validates that the property value is negative (less than zero).
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Number> KProperty1<T, R?>.isNegative(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.toDouble() < 0 }
}

/**
 * Validates that the property value is non-positive (less than or equal to zero).
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Number> KProperty1<T, R?>.isNonPositive(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.toDouble() <= 0 }
}
