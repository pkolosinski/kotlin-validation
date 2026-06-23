package dev.pkolosinski

import dev.pkolosinski.ValidationResult.Invalid
import dev.pkolosinski.ValidationResult.Valid
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Instant
import java.time.LocalDate
import kotlin.uuid.Uuid

private data class SampleDto(
    val userId: String?,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val phoneNumber: String,
    val email: String,
    val dateOfBirth: LocalDate,
    val createdAt: Instant,
)

private val validSample =
    SampleDto(
        userId = "30cc8519-f5b6-4748-9005-d42c10cdf669",
        firstName = "John",
        lastName = "Doe",
        age = 30,
        phoneNumber = "+1234567890",
        email = "john.doe@example.com",
        dateOfBirth = LocalDate.parse("1990-01-01"),
        createdAt = Instant.now(),
    )

class ValidationSpec :
    ShouldSpec({
        context("Validate SampleDto against given rules") {
            val validator = { dto: SampleDto ->
                validate(dto) {
                    ensureNotNull(
                        it.userId?.let { id -> Uuid.parseOrNull(id) },
                        "User ID must be a valid UUID",
                    )
                    ensureNotBlank(it.firstName, "First name cannot be blank")
                    ensureNotBlank(it.lastName, "Last name cannot be blank")
                    ensure(it.age > 0, "Age must be positive")
                    ensure(
                        it.phoneNumber.matches(Regex("^\\+?[1-9]\\d{1,14}$")),
                        "Invalid phone number format",
                    )
                    ensure(
                        it.email.matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")),
                        "Invalid email format",
                    )
                    ensure(
                        it.dateOfBirth.isBefore(LocalDate.now()),
                        "Date of birth must be in the past",
                    )
                }
            }

            should("return valid result") {
                val dto = validSample

                val result = validator(dto)

                result.shouldBeTypeOf<Valid<SampleDto>>()
                result.value shouldBe dto
            }

            should("return invalid result with single error") {
                val dto = validSample.copy(userId = "123")

                val result = validator(dto)

                result.shouldBeTypeOf<Invalid>()
                result.reasons() shouldBe listOf("User ID must be a valid UUID")
            }

            should("return invalid result with all errors") {
                val dto =
                    validSample.copy(
                        userId = "123",
                        firstName = "",
                        lastName = "",
                        age = -1,
                        phoneNumber = "invalid",
                        email = "invalid",
                        dateOfBirth = LocalDate.now().plusDays(1),
                    )

                val result = validator(dto)

                result.shouldBeTypeOf<Invalid>()
                result.reasons().shouldContainAll(
                    "User ID must be a valid UUID",
                    "First name cannot be blank",
                    "Last name cannot be blank",
                    "Age must be positive",
                    "Invalid phone number format",
                    "Invalid email format",
                    "Date of birth must be in the past",
                )
            }
        }
    })
