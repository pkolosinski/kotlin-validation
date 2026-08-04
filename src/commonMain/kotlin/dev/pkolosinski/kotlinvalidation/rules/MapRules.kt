package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the map is not empty.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Map<*, *>> KProperty1<T, R?>.isNotEmpty(
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.isNotEmpty() }
}

/**
 * Validates that the map has at least [min] entries.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Map<*, *>> KProperty1<T, R?>.minSize(
    min: Int,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size >= min }
}

/**
 * Validates that the map has at most [max] entries.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Map<*, *>> KProperty1<T, R?>.maxSize(
    max: Int,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size <= max }
}

/**
 * Validates that the map size is within the given [range].
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Map<*, *>> KProperty1<T, R?>.sizeIn(
    range: IntRange,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size in range }
}
