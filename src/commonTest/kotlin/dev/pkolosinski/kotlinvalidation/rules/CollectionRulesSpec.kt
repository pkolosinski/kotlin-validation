package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class CollectionRulesSpec : ShouldSpec({
    data class TestedClass(val collection: List<String>?)

    context("isNotEmpty") {
        val validator = buildValidator {
            TestedClass::collection.isNotEmpty()
        }
        withShoulds(
            null to true,
            listOf("value") to true,
            emptyList<String>() to false,
        ) { (collection, isValid) ->
            validator(TestedClass(collection)).isValid() shouldBe isValid
        }
    }

    context("minSize") {
        val validator = buildValidator {
            TestedClass::collection.minSize(3)
        }
        withShoulds(
            null to true,
            listOf("1", "2", "3") to true,
            listOf("1", "2", "3", "4") to true,
            emptyList<String>() to false,
            listOf("1", "2") to false,
        ) { (collection, isValid) ->
            validator(TestedClass(collection)).isValid() shouldBe isValid
        }
    }

    context("maxSize") {
        val validator = buildValidator {
            TestedClass::collection.maxSize(3)
        }
        withShoulds(
            null to true,
            emptyList<String>() to true,
            listOf("1", "2") to true,
            listOf("1", "2", "3") to true,
            listOf("1", "2", "3", "4") to false,
        ) { (collection, isValid) ->
            validator(TestedClass(collection)).isValid() shouldBe isValid
        }
    }

    context("sizeIn") {
        val validator = buildValidator {
            TestedClass::collection.sizeIn(1..3)
        }
        withShoulds(
            null to true,
            emptyList<String>() to false,
            listOf("1") to true,
            listOf("1", "2", "3") to true,
            listOf("1", "2", "3", "4") to false,
        ) { (collection, isValid) ->
            validator(TestedClass(collection)).isValid() shouldBe isValid
        }
    }

    context("hasUniqueElements") {
        val validator = buildValidator {
            TestedClass::collection.hasUniqueElements()
        }
        withShoulds(
            null to true,
            emptyList<String>() to true,
            listOf("1") to true,
            listOf("1", "2", "3") to true,
            listOf("1", "2", "2", "4") to false,
        ) { (collection, isValid) ->
            validator(TestedClass(collection)).isValid() shouldBe isValid
        }
    }
})
