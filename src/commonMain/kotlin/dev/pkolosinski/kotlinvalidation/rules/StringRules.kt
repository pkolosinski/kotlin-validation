package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationBuilder
import kotlin.reflect.KProperty1
import kotlin.uuid.Uuid

/**
 * Validates that the property value is not blank (non-empty and contains non-whitespace).
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isNotBlank(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.isNotBlank() }
}

/**
 * Validates that the property value is not empty.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isNotEmpty(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.isNotEmpty() }
}

/**
 * Validates that the property value matches the given regular expression.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.matches(
    pattern: String,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || Regex(pattern).matches(it) }
}

/**
 * Validates that the property value has at least [min] characters.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.minLength(
    min: Int,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.length >= min }
}

/**
 * Validates that the property value has at most [max] characters.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.maxLength(
    max: Int,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.length <= max }
}

/**
 * Validates that the property value length is within the given [range].
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.lengthIn(
    range: IntRange,
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.length in range }
}

/**
 * Validates that the property value contains only digits.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isDigitsOnly(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.all { char -> char.isDigit() } }
}

/**
 * Validates that the property value contains only alphabetic characters.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isLettersOnly(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.all { char -> char.isLetter() } }
}

/**
 * Validates that the property value is alphanumeric.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isAlphanumeric(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.all { char -> char.isLetterOrDigit() } }
}

/**
 * Validates that the property value contains only lowercase characters.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isLowercase(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.all { char -> char.isLowerCase() } }
}

/**
 * Validates that the property value contains only uppercase characters.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isUppercase(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || it.all { char -> char.isUpperCase() } }
}

/**
 * Validates that the property value is a valid email.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isEmail(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) {
        it == null ||
            Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(it)
    }
}

/**
 * Validates that the property value is a valid UUID.
 */
context(builder: ValidationBuilder<T>)
fun <T> KProperty1<T, String?>.isUuid(
    message: String? = null,
    errorCode: String? = null,
) = with(builder) {
    satisfies(message, errorCode) { it == null || Uuid.parseOrNull(it) != null }
}
