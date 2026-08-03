package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import kotlin.jvm.JvmName
import kotlin.reflect.KProperty1

internal typealias ValidationRule<T> = (T) -> List<ValidationError>

public class ValidationBuilder<T> internal constructor(
    private val pathPrefix: String? = null,
    private val failFast: Boolean = false,
) {
    private val rules: MutableList<ValidationRule<T>> = mutableListOf()

    public fun ensure(
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

    public fun <R> KProperty1<T, R>.satisfies(
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

    public fun <R : Any> KProperty1<T, R?>.validate(
        validationBlock: ValidationBuilder<R>.() -> Unit,
    ) {
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

    public fun <R> KProperty1<T, Collection<R>?>.eachSatisfies(
        message: String? = null,
        errorCode: String? = null,
        predicate: (R) -> Boolean,
    ): Unit = validateEach {
        ensure(message, errorCode, predicate)
    }

    @JvmName("eachMapEntrySatisfies")
    public fun <K, V : Any> KProperty1<T, Map<K, V?>?>.eachSatisfies(
        message: String? = null,
        errorCode: String? = null,
        predicate: (Map.Entry<K, V?>) -> Boolean,
    ): Unit = validateEntries {
        ensure(message, errorCode, predicate)
    }

    public fun <R : Any> KProperty1<T, Collection<R?>?>.validateEach(
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

    public fun <K, V : Any> KProperty1<T, Map<K, V?>?>.validateValues(
        validationBlock: ValidationBuilder<V>.() -> Unit,
    ) {
        val mapProperty = this
        val mapPath = appendPropToPath(mapProperty)
        val mapValueValidationBuilder = ValidationBuilder<V>()
            .apply(validationBlock)

        addMapRules(mapValueValidationBuilder) { instance ->
            mapProperty.get(instance).orEmpty().asSequence().mapNotNull { (key, value) ->
                value?.let { "$mapPath[$key]" to it }
            }
        }
    }

    public fun <K, V> KProperty1<T, Map<K, V>?>.validateKeys(
        validationBlock: ValidationBuilder<K>.() -> Unit,
    ) {
        val mapProperty = this
        val mapPath = appendPropToPath(mapProperty)
        val mapKeyValidationBuilder = ValidationBuilder<K>()
            .apply(validationBlock)

        addMapRules(mapKeyValidationBuilder) { instance ->
            mapProperty.get(instance).orEmpty().asSequence().mapNotNull { (key, _) ->
                key?.let { "$mapPath[$it]" to it }
            }
        }
    }

    public fun <K, V> KProperty1<T, Map<K, V?>?>.validateEntries(
        validationBlock: ValidationBuilder<Map.Entry<K, V?>>.() -> Unit,
    ) {
        val mapProperty = this
        val mapPath = appendPropToPath(mapProperty)
        val mapEntryValidationBuilder = ValidationBuilder<Map.Entry<K, V?>>()
            .apply(validationBlock)

        addMapRules(mapEntryValidationBuilder) { instance ->
            mapProperty.get(instance).orEmpty().asSequence().map { entry ->
                "$mapPath[${entry.key}]" to entry
            }
        }
    }

    private fun <R> addMapRules(
        elementValidationBuilder: ValidationBuilder<R>,
        elements: (T) -> Sequence<Pair<String, R>>,
    ) {
        val mapRules = elementValidationBuilder.rules.map { rule ->
            { instance: T ->
                val errors = elements(instance).map { (path, element) ->
                    rule(element).map { it.prependPath(path) }
                }
                if (failFast) {
                    errors.firstOrNull { it.isNotEmpty() }.orEmpty()
                } else {
                    errors.flatten().toList()
                }
            }
        }

        rules.addAll(mapRules)
    }

    internal fun addRule(rule: ValidationRule<T>) {
        rules.add(rule)
    }

    internal fun build(): Validator<T> {
        val ruleSnapshot = rules.toList()

        return { instance ->
            val errors: List<ValidationError> = if (failFast) {
                findFirstBrokenRuleOrEmpty(instance, ruleSnapshot)
            } else {
                ruleSnapshot.flatMap { rule -> rule(instance) }
            }
            if (errors.isEmpty()) {
                Valid(instance)
            } else {
                Invalid(errors)
            }
        }
    }

    private fun findFirstBrokenRuleOrEmpty(
        instance: T,
        ruleSnapshot: List<ValidationRule<T>>,
    ): List<ValidationError> = ruleSnapshot.asSequence()
        .map { rule -> rule(instance) }
        .firstOrNull { it.isNotEmpty() }
        .orEmpty()

    private fun appendPropToPath(property: KProperty1<T, *>): String =
        pathPrefix?.let { "$it.${property.name}" } ?: property.name
}

private fun ValidationError.prependPath(prefix: String): ValidationError = copy(
    path = path?.let { "$prefix.$it" } ?: prefix,
)
