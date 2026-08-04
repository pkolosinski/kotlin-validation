package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

private data class PublicApiSample(val name: String)

class PublicApiSpec : ShouldSpec({
    val validationBlock: ValidationBuilder<PublicApiSample>.() -> Unit = {
        PublicApiSample::name.satisfies(errorCode = "NAME_BLANK") {
            it.isNotBlank()
        }
    }
    val validator: Validator<PublicApiSample> = buildValidator(
        validationBlock = validationBlock,
    )

    should("support explicit public validator and DSL receiver types") {
        validator(PublicApiSample("Alice")) shouldBe Valid(PublicApiSample("Alice"))
        validator(PublicApiSample("")) shouldBe Invalid(
            listOf(
                ValidationError(
                    path = "name",
                    errorCode = "NAME_BLANK",
                ),
            ),
        )
    }

    should("treat empty validators and allOf identities as valid") {
        val value = "value"
        val emptyValidator: Validator<String> = buildValidator {}

        emptyValidator(value) shouldBe Valid(value)
        allOf<String>()(value) shouldBe Valid(value)
        allOf(emptyValidator)(value) shouldBe emptyValidator(value)
    }

    should("reuse a built validator without leaking state") {
        validator(PublicApiSample("first")).isValid() shouldBe true
        validator(PublicApiSample("")).isInvalid() shouldBe true
        validator(PublicApiSample("second")).isValid() shouldBe true
    }

    should("preserve composition error order and valid values") {
        val firstValidator = buildValidator<PublicApiSample> {
            ensure(errorCode = "FIRST") { it.name != "invalid" }
        }
        val secondValidator = buildValidator<PublicApiSample> {
            ensure(errorCode = "SECOND") { it.name != "invalid" }
        }
        val invalidValue = PublicApiSample("invalid")
        val expectedInvalid = Invalid(
            listOf(
                ValidationError(errorCode = "FIRST"),
                ValidationError(errorCode = "SECOND"),
            ),
        )

        (firstValidator and secondValidator)(invalidValue) shouldBe expectedInvalid
        allOf(firstValidator, secondValidator)(invalidValue) shouldBe expectedInvalid
        (firstValidator and secondValidator)(PublicApiSample("valid")) shouldBe
            Valid(PublicApiSample("valid"))
    }

    should("support default ValidationError fields") {
        ValidationError() shouldBe ValidationError(
            path = null,
            errorCode = null,
            message = null,
        )
    }
})
