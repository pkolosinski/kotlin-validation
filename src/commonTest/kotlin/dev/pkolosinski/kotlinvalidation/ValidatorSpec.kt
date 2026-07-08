package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldEqual
import io.kotest.matchers.types.shouldBeTypeOf

private data class SampleDto(
    val first: String?,
    val second: String? = null,
    val nested: SubSample? = null,
) {
    data class SubSample(
        val value: String? = null,
        val nested: SubSubSample? = null,
    ) {
        data class SubSubSample(val value: String? = null)
    }
}

class ValidatorSpec : ShouldSpec({
    context("Validate SampleDto against given rules") {
        val validationBlock: ValidationBuilder<SampleDto>.() -> Unit = {
            SampleDto::first.satisfies(
                message = "First cannot be null or blank",
                errorCode = "FIRST_BLANK",
            ) {
                !it.isNullOrBlank()
            }
            SampleDto::nested.validate {
                SampleDto.SubSample::value.satisfies(
                    message = "Value cannot be blank",
                    errorCode = "SUB_VALUE_BLANK",
                ) {
                    it == null || it.isNotBlank()
                }
                SampleDto.SubSample::nested.validate {
                    SampleDto.SubSample.SubSubSample::value.satisfies(
                        message = "Nested value cannot be blank",
                        errorCode = "SUB_SUB_VALUE_BLANK",
                    ) {
                        it == null || it.isNotBlank()
                    }
                }
            }
            ensure(
                message = "First and second must not be equal",
                errorCode = "FIRST_SECOND_NOT_EQUAL",
            ) {
                it.first != it.second
            }
        }
        val validator = buildValidator(validationBlock = validationBlock)

        should("return valid result when all rules are satisfied") {
            // given
            val dto = SampleDto(
                first = "first",
                second = "second",
                nested = SampleDto.SubSample(
                    value = "value",
                    nested = SampleDto.SubSample.SubSubSample(value = "nestedValue"),
                ),
            )

            // when
            val result = validator(dto)

            // then
            result.shouldBeTypeOf<Valid<SampleDto>>()
            result.value shouldEqual dto
        }

        should("return invalid result when property rule is violated") {
            // given
            val dto = SampleDto(
                first = "",
                second = "second",
            )
            val expectedError = ValidationError(
                path = "first",
                errorCode = "FIRST_BLANK",
                message = "First cannot be null or blank",
            )

            // when
            val result = validator(dto)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize 1
            result.errors.first() shouldEqual expectedError
        }

        should("return invalid result when non-property rule is violated") {
            // given
            val dto = SampleDto(
                first = "text",
                second = "text",
            )
            val expectedError = ValidationError(
                path = null,
                errorCode = "FIRST_SECOND_NOT_EQUAL",
                message = "First and second must not be equal",
            )

            // when
            val result = validator(dto)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize 1
            result.errors.first() shouldEqual expectedError
        }

        should("return invalid result when nested property rule is violated") {
            // given
            val dto = SampleDto(
                first = "first",
                second = "second",
                nested = SampleDto.SubSample(
                    value = "value",
                    nested = SampleDto.SubSample.SubSubSample(value = ""),
                ),
            )
            val expectedError = ValidationError(
                path = "nested.nested.value",
                errorCode = "SUB_SUB_VALUE_BLANK",
                message = "Nested value cannot be blank",
            )

            // when
            val result = validator(dto)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize 1
            result.errors.first() shouldEqual expectedError
        }

        should("return invalid result with aggregated errors") {
            // given
            val dto = SampleDto(
                first = "",
                second = "",
                nested = SampleDto.SubSample(
                    value = "",
                    nested = SampleDto.SubSample.SubSubSample(value = ""),
                ),
            )
            val expectedErrors = listOf(
                ValidationError(
                    path = "first",
                    errorCode = "FIRST_BLANK",
                    message = "First cannot be null or blank",
                ),
                ValidationError(
                    path = "nested.value",
                    errorCode = "SUB_VALUE_BLANK",
                    message = "Value cannot be blank",
                ),
                ValidationError(
                    path = "nested.nested.value",
                    errorCode = "SUB_SUB_VALUE_BLANK",
                    message = "Nested value cannot be blank",
                ),
                ValidationError(
                    path = null,
                    errorCode = "FIRST_SECOND_NOT_EQUAL",
                    message = "First and second must not be equal",
                ),
            )

            // when
            val result = validator(dto)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize expectedErrors.size
            result.errors shouldContainExactlyInAnyOrder expectedErrors
        }

        should("return the same validation result when validating using function and builder") {
            // given
            val dto = SampleDto(
                first = "",
                second = "",
                nested = SampleDto.SubSample(
                    value = "",
                    nested = SampleDto.SubSample.SubSubSample(value = ""),
                ),
            )

            // when
            val builderResult = buildValidator(validationBlock = validationBlock).invoke(dto)
            val funResult = validate(dto, validationBlock = validationBlock)

            // then
            builderResult shouldEqual funResult
        }

        should("return only the first error when fail-fast is enabled") {
            // given
            val dto = SampleDto(
                first = "",
                second = "",
                nested = SampleDto.SubSample(
                    value = "",
                    nested = SampleDto.SubSample.SubSubSample(value = ""),
                ),
            )
            val expectedError = ValidationError(
                path = "first",
                errorCode = "FIRST_BLANK",
                message = "First cannot be null or blank",
            )

            // when
            val result = validate(dto, failFast = true, validationBlock)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize 1
            result.errors.first() shouldEqual expectedError
        }

        should("return valid result when fail-fast is enabled and all rules are satisfied") {
            // given
            val dto = SampleDto(
                first = "first",
                second = "second",
                nested = SampleDto.SubSample(
                    value = "value",
                    nested = SampleDto.SubSample.SubSubSample(value = "nestedValue"),
                ),
            )

            // when
            val result = validate(dto, failFast = true, validationBlock)

            // then
            result.shouldBeTypeOf<Valid<SampleDto>>()
            result.value shouldEqual dto
        }

        should("return nested errors when fail-fast is enabled and a nested rule fails") {
            // given
            val dto = SampleDto(
                first = "first",
                second = "second",
                nested = SampleDto.SubSample(
                    value = "",
                    nested = SampleDto.SubSample.SubSubSample(value = ""),
                ),
            )
            val expectedError = ValidationError(
                path = "nested.value",
                errorCode = "SUB_VALUE_BLANK",
                message = "Value cannot be blank",
            )

            // when
            val result = validate(dto, failFast = true, validationBlock)

            // then
            result.shouldBeTypeOf<Invalid>()
            result.errors shouldHaveSize 1
            result.errors.first() shouldEqual expectedError
        }
    }

    context("Compose validators") {
        val validator = buildValidator {
            SampleDto::first.satisfies {
                !it.isNullOrBlank()
            }
            SampleDto::second.satisfies {
                !it.isNullOrBlank()
            }
            ensure { it.nested != null }
        }
        val invalidDto = SampleDto(
            first = "",
            second = null,
            nested = null,
        )

        should("validate using composed 2 validators") {
            // given
            val firstValidator = buildValidator {
                SampleDto::first.satisfies {
                    !it.isNullOrBlank()
                }
            }
            val secondValidator = buildValidator {
                SampleDto::second.satisfies {
                    !it.isNullOrBlank()
                }
                ensure { it.nested != null }
            }
            val composedValidators = firstValidator and secondValidator

            // expect
            validator(invalidDto) shouldEqual composedValidators(invalidDto)
        }

        should("validate using composed multiple validators") {
            // given
            val validators = arrayOf(
                buildValidator {
                    SampleDto::first.satisfies {
                        !it.isNullOrBlank()
                    }
                },
                buildValidator {
                    SampleDto::second.satisfies {
                        !it.isNullOrBlank()
                    }
                },
                buildValidator<SampleDto> {
                    ensure { it.nested != null }
                },
            )
            val composedValidators = allOf(*validators)

            // expect
            validator(invalidDto) shouldEqual composedValidators(invalidDto)
        }
    }
})
