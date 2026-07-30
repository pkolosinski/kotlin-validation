package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import dev.pkolosinski.kotlinvalidation.fixtures.invalidResult
import dev.pkolosinski.kotlinvalidation.fixtures.validResult
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ValidationResultSpec : ShouldSpec({
    context("ValidationResult convenience properties") {
        should("report isValid for valid results") {
            validResult.isValid() shouldBe true
            validResult.isInvalid() shouldBe false
        }

        should("report isInvalid for invalid results") {
            invalidResult.isValid() shouldBe false
            invalidResult.isInvalid() shouldBe true
        }
    }

    context("ValidationResult.getOrNull") {
        should("return the value for valid results") {
            validResult.getOrNull() shouldBe validResult.value
        }

        should("return null for invalid results") {
            invalidResult.getOrNull() shouldBe null
        }
    }

    context("ValidationResult.errorsOrEmpty") {
        should("return an empty list for valid results") {
            validResult.errorsOrEmpty() shouldBe emptyList()
        }

        should("return errors for invalid results") {
            invalidResult.errorsOrEmpty() shouldBe invalidResult.errors
        }
    }

    context("ValidationResult.map") {
        should("transform the value for valid results") {
            // when
            val result = validResult.map(String::length)

            // then
            result shouldBe Valid(5)
        }

        should("pass through invalid results") {
            // when
            val result = invalidResult.map(String::length)

            // then
            result shouldBe invalidResult
        }
    }

    context("ValidationResult.mapErrors") {
        should("transform the error list for invalid results") {
            // given
            val errors = listOf(
                ValidationError("first", "first error"),
                ValidationError("second", "second error"),
            )

            // when
            val result = invalidResult.copy(errors = errors)
                .mapErrors { it.reversed() }

            // then
            result shouldBe Invalid(errors.reversed())
        }

        should("pass through valid results") {
            // when
            val result = validResult.mapErrors { it.reversed() }

            // then
            result shouldBe validResult
        }
    }

    context("ValidationResult.fold") {
        should("invoke the valid branch for valid results") {
            // when
            val result = validResult.fold(
                ifValid = { it.length },
                ifInvalid = { -1 },
            )

            // then
            result shouldBe 5
        }

        should("invoke the invalid branch for invalid results") {
            // given
            val errors = listOf(
                ValidationError("first", "first error"),
                ValidationError("second", "second error"),
            )

            // when
            val result = invalidResult.copy(errors = errors)
                .fold(
                    ifValid = { 0 },
                    ifInvalid = { it.size },
                )

            // then
            result shouldBe 2
        }
    }

    context("ValidationResult.onValid") {
        should("invoke the action for valid results") {
            // given
            var captured: String? = null

            // when
            val result = validResult.onValid { captured = it }

            // then
            captured shouldBe validResult.value
            result shouldBe validResult
        }

        should("not invoke the action for invalid results") {
            // given
            var invoked = false

            // when
            val result = invalidResult.onValid { invoked = true }

            // then
            invoked shouldBe false
            result shouldBe invalidResult
        }
    }

    context("ValidationResult.onInvalid") {
        should("invoke the action for invalid results") {
            // given
            var captured: List<ValidationError>? = null

            // when
            val result = invalidResult.onInvalid { captured = it }

            // then
            captured shouldBe invalidResult.errors
            result shouldBe invalidResult
        }

        should("not invoke the action for valid results") {
            // given
            var invoked = false

            // when
            val result = validResult.onInvalid { invoked = true }

            // then
            invoked shouldBe false
            result shouldBe validResult
        }
    }
})
