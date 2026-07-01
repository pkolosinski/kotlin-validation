package dev.pkolosinski.kotlinvalidation

sealed interface ValidationResult<T> {
    data class Valid<T>(val value: T) : ValidationResult<T>

    data class Invalid(val errors: List<ValidationError>) : ValidationResult<Nothing> {
        fun reasons(): List<String> = errors.map { it.message }
    }
}
