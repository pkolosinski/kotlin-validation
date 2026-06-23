package dev.pkolosinski

typealias ValidationState = MutableList<ValidationError>

inline fun <T> validate(
    value: T,
    validateBlock: ValidationState.(T) -> Unit,
): ValidationResult<out T> {
    val validationState = mutableListOf<ValidationError>().apply {
        validateBlock(value)
    }

    @Suppress("KotlinConstantConditions")
    return if (validationState.isEmpty()) {
        ValidationResult.Valid(value)
    } else {
        ValidationResult.Invalid(validationState)
    }
}

fun ValidationState.ensure(condition: Boolean, errorMsg: String) {
    if (!condition) {
        this.add(ValidationError(errorMsg))
    }
}

fun ValidationState.ensureNotNull(value: Any?, errorMsg: String) = ensure(value != null, errorMsg)

fun ValidationState.ensureNotBlank(value: String?, errorMsg: String) =
    ensure(!value.isNullOrBlank(), errorMsg)
