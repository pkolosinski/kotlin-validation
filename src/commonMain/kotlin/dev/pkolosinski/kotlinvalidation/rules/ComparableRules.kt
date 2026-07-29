package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the property value is at least [min].
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Comparable<R>> KProperty1<T, R?>.min(
    min: R,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it >= min }
}

/**
 * Validates that the property value is at most [max].
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Comparable<R>> KProperty1<T, R?>.max(
    max: R,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it <= max }
}

/**
 * Validates that the property value is within the specified [range].
 */
context(builder: ValidationBuilder<T>)
fun <T, R : Comparable<R>> KProperty1<T, R?>.range(
    range: ClosedRange<R>,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it in range }
}
