package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import kotlin.reflect.KProperty1

internal typealias ValidationRule<T> = (T) -> ValidationError?

class ValidationBuilder<T> internal constructor(
    private val pathPrefix: String? = null,
    private val failFast: Boolean = false,
) {
    private val rules: MutableList<ValidationRule<T>> = mutableListOf()

    internal fun addRule(rule: ValidationRule<T>) {
        rules.add(rule)
    }

    internal fun build(): Validator<T> = { instance ->
        val errors: List<ValidationError> = if (failFast) {
            val error = rules.firstNotNullOfOrNull { rule -> rule(instance) }
            listOfNotNull(error)
        } else {
            rules.mapNotNull { rule -> rule(instance) }
        }
        if (errors.isEmpty()) {
            Valid(instance)
        } else {
            Invalid(errors)
        }
    }

    fun <R : Any> KProperty1<T, R?>.validate(validationBlock: ValidationBuilder<R>.() -> Unit) {
        val property = this
        val nestedBuilder = ValidationBuilder<R>(pathPrefix = appendPropToPath(property))
            .apply(validationBlock)
        val nestedPropRules: List<ValidationRule<T>> = nestedBuilder.rules
            .mapNotNull { rule ->
                { instance ->
                    property.get(instance)?.let { rule(it) }
                }
            }
        rules.addAll(nestedPropRules)
    }

    fun <R> KProperty1<T, R>.satisfies(
        message: String? = null,
        errorCode: String? = null,
        predicate: (R) -> Boolean,
    ) {
        val property = this
        addRule { instance ->
            instance?.takeUnless { predicate(property.get(it)) }
                ?.let {
                    ValidationError(
                        path = appendPropToPath(property),
                        errorCode = errorCode,
                        message = message,
                    )
                }
        }
    }

    fun ensure(
        message: String? = null,
        errorCode: String? = null,
        predicate: (T) -> Boolean,
    ) {
        addRule { instance ->
            instance?.takeUnless(predicate)
                ?.let {
                    ValidationError(path = pathPrefix, errorCode = errorCode, message = message)
                }
        }
    }

    private fun appendPropToPath(property: KProperty1<T, *>): String =
        pathPrefix?.let { "$it.${property.name}" } ?: property.name
}
