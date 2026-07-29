package dev.pkolosinski.kotlinvalidation

data class ValidationError(
    val path: String? = null,
    val errorCode: String? = null,
    val message: String? = null,
)
