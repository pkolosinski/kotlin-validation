package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class ComparableRulesSpec : ShouldSpec({
    data class TestedClass(
        val intValue: Int? = null,
        val dateValue: LocalDate? = null,
    )

    context("min int") {
        val validator = buildValidator {
            TestedClass::intValue.min(10)
        }
        withShoulds(
            null to true,
            10 to true,
            11 to true,
            9 to false,
        ) { (value, isValid) ->
            validator(TestedClass(intValue = value)).isValid() shouldBe isValid
        }
    }

    context("min date") {
        val validator = buildValidator {
            TestedClass::dateValue.min(LocalDate(2020, 1, 1))
        }
        withShoulds(
            null to true,
            LocalDate(2020, 1, 1) to true,
            LocalDate(2020, 1, 2) to true,
            LocalDate(2019, 12, 31) to false,
        ) { (value, isValid) ->
            validator(TestedClass(dateValue = value)).isValid() shouldBe isValid
        }
    }

    context("max int") {
        val validator = buildValidator {
            TestedClass::intValue.max(10)
        }
        withShoulds(
            null to true,
            10 to true,
            9 to true,
            11 to false,
        ) { (value, isValid) ->
            validator(TestedClass(intValue = value)).isValid() shouldBe isValid
        }
    }

    context("max date") {
        val validator = buildValidator {
            TestedClass::dateValue.max(LocalDate(2020, 1, 1))
        }
        withShoulds(
            null to true,
            LocalDate(2020, 1, 1) to true,
            LocalDate(2019, 12, 31) to true,
            LocalDate(2020, 1, 2) to false,
        ) { (value, isValid) ->
            validator(TestedClass(dateValue = value)).isValid() shouldBe isValid
        }
    }

    context("int range") {
        val validator = buildValidator {
            TestedClass::intValue.range(1..10)
        }
        withShoulds(
            null to true,
            1 to true,
            10 to true,
            5 to true,
            0 to false,
            11 to false,
        ) { (value, isValid) ->
            validator(TestedClass(value, null)).isValid() shouldBe isValid
        }
    }

    context("date range") {
        val validator = buildValidator {
            TestedClass::dateValue.range(LocalDate(2020, 1, 1)..LocalDate(2020, 1, 10))
        }
        withShoulds(
            null to true,
            LocalDate(2020, 1, 1) to true,
            LocalDate(2020, 1, 10) to true,
            LocalDate(2020, 1, 5) to true,
            LocalDate(2019, 12, 31) to false,
            LocalDate(2020, 1, 11) to false,
        ) { (value, isValid) ->
            validator(TestedClass(null, value)).isValid() shouldBe isValid
        }
    }
})
