package dev.pkolosinski.kotlinvalidation

import io.kotest.matchers.types.shouldBeInstanceOf

internal fun <T> ValidationResult<T>.shouldBeValid() =
    this.shouldBeInstanceOf<ValidationResult.Valid<T>>()

internal fun ValidationResult<*>.shouldBeInvalid() =
    this.shouldBeInstanceOf<ValidationResult.Invalid>()
