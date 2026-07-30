package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import kotlin.reflect.KProperty1

internal typealias ValidationRule<T> = (T) -> List<ValidationError>

class ValidationBuilder<T> internal constructor(
    private val pathPrefix: String? = null,
    private val failFast: Boolean = false,
) {
    private val rules: MutableList<ValidationRule<T>> = mutableListOf()

    fun ensure(
        message: String? = null,
        errorCode: String? = null,
        predicate: (T) -> Boolean,
    ) {
        addRule { instance ->
            instance?.takeUnless(predicate)
                ?.let {
                    listOf(
                        ValidationError(
                            path = pathPrefix,
                            errorCode = errorCode,
                            message = message,
                        ),
                    )
                }.orEmpty()
        }
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
                    listOf(
                        ValidationError(
                            path = appendPropToPath(property),
                            errorCode = errorCode,
                            message = message,
                        ),
                    )
                }.orEmpty()
        }
    }

    fun <R : Any> KProperty1<T, R?>.validate(validationBlock: ValidationBuilder<R>.() -> Unit) {
        val property = this
        val propertyValidationBuilder = ValidationBuilder<R>(
            pathPrefix = appendPropToPath(property),
        )
            .apply(validationBlock)

        val propertyRules = propertyValidationBuilder.rules.map { rule ->
            { instance: T ->
                property.get(instance)?.let { rule(it) }.orEmpty()
            }
        }

        rules.addAll(propertyRules)
    }

    fun <R> KProperty1<T, Collection<R>?>.eachSatisfies(
        message: String? = null,
        errorCode: String? = null,
        predicate: (R) -> Boolean,
    ) = validateEach {
        ensure(message, errorCode, predicate)
    }

    fun <R : Any> KProperty1<T, Collection<R?>?>.validateEach(
        validationBlock: ValidationBuilder<R>.() -> Unit,
    ) {
        val collectionProperty = this
        val collectionPath = appendPropToPath(collectionProperty)
        val collectionItemValidationBuilder = ValidationBuilder<R>()
            .apply(validationBlock)

        val collectionRules = collectionItemValidationBuilder.rules.map { rule ->
            { instance: T ->
                val items = collectionProperty.get(instance).orEmpty()
                if (failFast) {
                    items.asSequence()
                        .filterNotNull()
                        .mapIndexed { index, item ->
                            rule(item).map { it.prependPath("$collectionPath[$index]") }
                        }
                        .firstOrNull { it.isNotEmpty() }
                        .orEmpty()
                } else {
                    items.filterNotNull()
                        .flatMapIndexed { index, item ->
                            rule(item).map { it.prependPath("$collectionPath[$index]") }
                        }
                }
            }
        }

        rules.addAll(collectionRules)
    }

    fun <K, V : Any> KProperty1<T, Map<K, V?>?>.validateValues(
        validationBlock: ValidationBuilder<V>.() -> Unit,
    ) {
        val mapProperty = this
        val mapPath = appendPropToPath(mapProperty)
        val mapValueValidationBuilder = ValidationBuilder<V>()
            .apply(validationBlock)

        val mapRules = mapValueValidationBuilder.rules.map { rule ->
            { instance: T ->
                mapProperty.get(instance).orEmpty()
                    .flatMap { (key, value) ->
                        value?.let { v ->
                            rule(v).map { it.prependPath("$mapPath[$key]") }
                        }.orEmpty()
                    }
            }
        }

        rules.addAll(mapRules)
    }

    internal fun addRule(rule: ValidationRule<T>) {
        rules.add(rule)
    }

    internal fun build(): Validator<T> = { instance ->
        val errors: List<ValidationError> = if (failFast) {
            findFirstBrokenRuleOrEmpty(instance)
        } else {
            rules.flatMap { rule -> rule(instance) }
        }
        if (errors.isEmpty()) {
            Valid(instance)
        } else {
            Invalid(errors)
        }
    }

    private fun findFirstBrokenRuleOrEmpty(instance: T): List<ValidationError> = rules.asSequence()
        .map { rule -> rule(instance) }
        .firstOrNull { it.isNotEmpty() }
        .orEmpty()

    private fun appendPropToPath(property: KProperty1<T, *>): String =
        pathPrefix?.let { "$it.${property.name}" } ?: property.name
}

private fun ValidationError.prependPath(prefix: String): ValidationError = copy(
    path = path?.let { "$prefix.$it" } ?: prefix,
)
