package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the property value is at least [min].
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Comparable<R>> KProperty1<T, R?>.min(
    min: R,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it >= min }
}

/**
 * Validates that the property value is at most [max].
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Comparable<R>> KProperty1<T, R?>.max(
    max: R,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it <= max }
}

/**
 * Validates that the property value is within the specified [range].
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Comparable<R>, Range> KProperty1<T, R?>.inRange(
    range: Range,
    message: String? = null,
    errorCode: String? = null,
): Unit where Range : ClosedRange<R>, Range : OpenEndRange<R> = with(builder) {
    satisfies(message, errorCode) { it == null || it in range }
}
