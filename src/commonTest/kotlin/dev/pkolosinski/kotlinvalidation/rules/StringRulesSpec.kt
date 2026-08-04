package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.buildValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class StringRulesSpec : ShouldSpec({
    data class TestedClass(val value: String?)

    context("isNotBlank") {
        val validator = buildValidator {
            TestedClass::value.isNotBlank()
        }
        withShoulds(
            null to true,
            "value" to true,
            "a" to true,
            "" to false,
            " " to false,
            "  " to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isNotEmpty") {
        val validator = buildValidator {
            TestedClass::value.isNotEmpty()
        }
        withShoulds(
            null to true,
            "value" to true,
            " " to true,
            "" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("matches") {
        val validator = buildValidator {
            TestedClass::value.matches("[A-Z]+")
        }
        withShoulds(
            null to true,
            "ABC" to true,
            "" to false,
            "abc" to false,
            "A1" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("minLength") {
        val validator = buildValidator {
            TestedClass::value.minLength(3)
        }
        withShoulds(
            null to true,
            "abc" to true,
            "abcd" to true,
            "ab" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("maxLength") {
        val validator = buildValidator {
            TestedClass::value.maxLength(3)
        }
        withShoulds(
            null to true,
            "" to true,
            "abc" to true,
            "abcd" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("lengthIn") {
        val validator = buildValidator {
            TestedClass::value.lengthIn(2..4)
        }
        withShoulds(
            null to true,
            "ab" to true,
            "abcd" to true,
            "a" to false,
            "abcde" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isDigitsOnly") {
        val validator = buildValidator {
            TestedClass::value.isDigitsOnly()
        }
        withShoulds(
            null to true,
            "" to true,
            "123" to true,
            "12a" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isLettersOnly") {
        val validator = buildValidator {
            TestedClass::value.isLettersOnly()
        }
        withShoulds(
            null to true,
            "" to true,
            "abc" to true,
            "Aćźłó" to true,
            "a1" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isAlphanumeric") {
        val validator = buildValidator {
            TestedClass::value.isAlphanumeric()
        }
        withShoulds(
            null to true,
            "" to true,
            "abć123" to true,
            "abc-123" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isLowercase") {
        val validator = buildValidator {
            TestedClass::value.isLowercase()
        }
        withShoulds(
            null to true,
            "" to true,
            "abć" to true,
            "Abc" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isUppercase") {
        val validator = buildValidator {
            TestedClass::value.isUppercase()
        }
        withShoulds(
            null to true,
            "" to true,
            "ABĆ" to true,
            "Abc" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isEmail") {
        val validator = buildValidator {
            TestedClass::value.isEmail()
        }
        withShoulds(
            null to true,
            "user@example.com" to true,
            "user.name+tag@example.co.uk" to true,
            "" to false,
            "user" to false,
            "user@" to false,
            "@example.com" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }

    context("isUuid") {
        val validator = buildValidator {
            TestedClass::value.isUuid()
        }
        withShoulds(
            null to true,
            "550e8400-e29b-41d4-a716-446655440000" to true,
            "" to false,
            "not-a-uuid" to false,
        ) { (value, isValid) ->
            validator(TestedClass(value)).isValid() shouldBe isValid
        }
    }
})
