package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class CommonRulesSpec : ShouldSpec({
    data class TestedClass(val value: String?)

    context("isNotNull") {
        val validator = buildValidator {
            TestedClass::value.isNotNull()
        }
        withShoulds(
            null to false,
            "value" to true,
            "" to true,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isNull") {
        val validator = buildValidator {
            TestedClass::value.isNull()
        }
        withShoulds(
            null to true,
            "value" to false,
            "" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }
})
