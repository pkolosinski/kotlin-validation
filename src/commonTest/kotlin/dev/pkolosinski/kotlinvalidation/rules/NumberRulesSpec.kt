package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class NumberRulesSpec : ShouldSpec({
    data class TestedClass(
        val intValue: Int? = null,
        val doubleValue: Double? = null,
    )

    context("isPositive") {
        val validator = buildValidator {
            TestedClass::intValue.isPositive()
            TestedClass::doubleValue.isPositive()
        }
        withShoulds(
            TestedClass(null, null) to true,
            TestedClass(1, 1.0) to true,
            TestedClass(0, 0.0) to false,
            TestedClass(-1, -1.0) to false,
        ) { (dto, isValid) ->
            validator(dto).isValid() shouldBe isValid
        }
    }

    context("isNonNegative") {
        val validator = buildValidator {
            TestedClass::intValue.isNonNegative()
            TestedClass::doubleValue.isNonNegative()
        }
        withShoulds(
            TestedClass(null, null) to true,
            TestedClass(0, 0.0) to true,
            TestedClass(1, 1.0) to true,
            TestedClass(-1, -1.0) to false,
        ) { (dto, isValid) ->
            validator(dto).isValid() shouldBe isValid
        }
    }

    context("isNegative") {
        val validator = buildValidator {
            TestedClass::intValue.isNegative()
            TestedClass::doubleValue.isNegative()
        }
        withShoulds(
            TestedClass(null, null) to true,
            TestedClass(-1, -0.1) to true,
            TestedClass(0, 0.0) to false,
            TestedClass(1, 1.0) to false,
        ) { (dto, isValid) ->
            validator(dto).isValid() shouldBe isValid
        }
    }

    context("isNonPositive") {
        val validator = buildValidator {
            TestedClass::intValue.isNonPositive()
            TestedClass::doubleValue.isNonPositive()
        }
        withShoulds(
            TestedClass(null, null) to true,
            TestedClass(0, 0.0) to true,
            TestedClass(-1, -1.0) to true,
            TestedClass(1, 1.0) to false,
        ) { (dto, isValid) ->
            validator(dto).isValid() shouldBe isValid
        }
    }
})
