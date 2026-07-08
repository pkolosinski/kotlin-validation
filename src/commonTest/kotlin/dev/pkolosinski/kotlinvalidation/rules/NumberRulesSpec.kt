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

    context("positive") {
        val validator = buildValidator {
            TestedClass::intValue.positive()
            TestedClass::doubleValue.positive()
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

    context("nonNegative") {
        val validator = buildValidator {
            TestedClass::intValue.nonNegative()
            TestedClass::doubleValue.nonNegative()
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

    context("negative") {
        val validator = buildValidator {
            TestedClass::intValue.negative()
            TestedClass::doubleValue.negative()
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

    context("nonPositive") {
        val validator = buildValidator {
            TestedClass::intValue.nonPositive()
            TestedClass::doubleValue.nonPositive()
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
