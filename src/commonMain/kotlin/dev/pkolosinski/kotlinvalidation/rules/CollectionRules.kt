package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1

/**
 * Validates that the collection is not empty.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Collection<*>> KProperty1<T, R?>.isNotEmpty(
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.isNotEmpty() }
}

/**
 * Validates that the collection has at least [min] elements.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Collection<*>> KProperty1<T, R?>.minSize(
    min: Int,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size >= min }
}

/**
 * Validates that the collection has at most [max] elements.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Collection<*>> KProperty1<T, R?>.maxSize(
    max: Int,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size <= max }
}

/**
 * Validates that the collection size is within the given [range].
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Collection<*>> KProperty1<T, R?>.sizeIn(
    range: IntRange,
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.size in range }
}

/**
 * Validates that the collection has only unique elements.
 */
context(builder: ValidationBuilder<T>)
public fun <T, R : Collection<*>> KProperty1<T, R?>.hasUniqueElements(
    message: String? = null,
    errorCode: String? = null,
): Unit = with(builder) {
    satisfies(message, errorCode) { it == null || it.distinct().size == it.size }
}
