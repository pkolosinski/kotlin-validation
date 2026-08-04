package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class MapRulesSpec : ShouldSpec({
    data class TestedClass(val values: Map<String, Int>?)

    context("isNotEmpty") {
        val validator = buildValidator {
            TestedClass::values.isNotEmpty()
        }

        withShoulds(
            null to true,
            emptyMap<String, Int>() to false,
            mapOf("one" to 1) to true,
        ) { (values, isValid) ->
            validator(TestedClass(values)).isValid() shouldBe isValid
        }
    }

    context("minSize") {
        val validator = buildValidator {
            TestedClass::values.minSize(2)
        }

        withShoulds(
            null to true,
            emptyMap<String, Int>() to false,
            mapOf("one" to 1) to false,
            mapOf("one" to 1, "two" to 2) to true,
        ) { (values, isValid) ->
            validator(TestedClass(values)).isValid() shouldBe isValid
        }
    }

    context("maxSize") {
        val validator = buildValidator {
            TestedClass::values.maxSize(2)
        }

        withShoulds(
            null to true,
            emptyMap<String, Int>() to true,
            mapOf("one" to 1, "two" to 2) to true,
            mapOf("one" to 1, "two" to 2, "three" to 3) to false,
        ) { (values, isValid) ->
            validator(TestedClass(values)).isValid() shouldBe isValid
        }
    }

    context("sizeIn") {
        val validator = buildValidator {
            TestedClass::values.sizeIn(1..2)
        }

        withShoulds(
            null to true,
            emptyMap<String, Int>() to false,
            mapOf("one" to 1) to true,
            mapOf("one" to 1, "two" to 2) to true,
            mapOf("one" to 1, "two" to 2, "three" to 3) to false,
        ) { (values, isValid) ->
            validator(TestedClass(values)).isValid() shouldBe isValid
        }
    }
})
