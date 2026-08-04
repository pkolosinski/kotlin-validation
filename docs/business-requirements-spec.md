# Kotlin Validation Business Requirements Specification

## Product summary

Kotlin Validation is a Kotlin Multiplatform validation library for validating DTOs and domain data
with an idiomatic Kotlin API. The product must support a declarative DSL as its primary experience
and provide annotation-based validation for users who prefer Spring/Bean Validation style workflows.

The primary market differentiator is full Kotlin Multiplatform support combined with type-safe
Kotlin DSL rules based on `KProperty` field references, such as `User::email`.

## Goals

1. Provide a concise, pleasant, idiomatic Kotlin API for data validation.
2. Work from shared KMP code without platform-specific dependencies in `commonMain`.
3. Return structured validation results that applications can map to APIs, UI forms, logs, and
   localized messages.
4. Support both simple field validation and complex object graph validation.
5. Allow users to choose fail-fast validation or collect-all validation.
6. Offer extensibility for custom business rules and future ecosystem integrations.

## Non-goals for the initial MVP

1. Replacing every feature of Jakarta Bean Validation in the first release.
2. Shipping all platform integrations in the first release.
3. Requiring reflection at runtime.
4. Requiring JVM-only dependencies in the core module.
5. Choosing the final public brand name without a separate product decision.

## Product naming requirement

The product needs a final public name before release. Candidate names from the brainstorm are:

| Candidate  | Meaning                | Positioning                       |
|------------|------------------------|-----------------------------------|
| Kward      | Kotlin + Guard         | Short, modern, safety-focused     |
| Valiko     | Validation + Kotlin    | Memorable open-source brand       |
| Veriko     | Verify + Kotlin        | Professional and enterprise-ready |
| Kalliper   | Caliper precision tool | Technical, precision-oriented     |
| Kotilidate | Kotlin + Validate      | Playful, direct, searchable       |

The implementation may continue under the repository name `kotlin-validation` until a name is
selected.

## User segments

| Segment                        | Need                                                                          |
|--------------------------------|-------------------------------------------------------------------------------|
| KMP library authors            | Validate shared models in common code without platform-specific dependencies. |
| Backend developers             | Validate request DTOs and domain commands with structured errors.             |
| Android and Compose developers | Validate form state and display field-level errors.                           |
| Spring developers              | Use familiar annotation-based validation patterns.                            |
| Functional Kotlin developers   | Interoperate with Arrow-style `Either` or `Validated` workflows.              |

## Priority definitions

| Priority | Meaning                                                                   |
|----------|---------------------------------------------------------------------------|
| Must     | Required for the first useful product release.                            |
| Should   | Important after the core release; may be delivered incrementally.         |
| Could    | Ecosystem or advanced capability that can follow once the core is stable. |

## Must-have business requirements

| ID     | Requirement                                                    | Acceptance criteria                                                                                                            |
|--------|----------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| BR-001 | The library must be Kotlin Multiplatform.                      | Core validation APIs compile from `commonMain` and are usable by JVM, Android, and native targets configured in the project.   |
| BR-002 | The core module must avoid platform-specific dependencies.     | `commonMain` has no JVM-only, Android-only, Spring, Ktor, reflection, or platform runtime dependencies.                        |
| BR-003 | The library must provide a declarative validation DSL.         | Users can define validation rules in Kotlin code in a readable block-oriented style.                                           |
| BR-004 | The DSL must support type-safe field references.               | Users can associate rules with fields through `KProperty` references so errors can include stable field paths.                 |
| BR-005 | The library must provide built-in validators.                  | The core release includes `notNull`, `notBlank`, `minLength`, `maxLength`, `min`, `max`, `regex`, `email`, `url`, and `range`. |
| BR-006 | The library must validate nullable and non-null values safely. | Nullable validation has explicit APIs and does not force unsafe casts or ambiguous behavior.                                   |
| BR-007 | The library must support nested object validation.             | A validator can delegate to another validator for a nested object and preserve paths such as `address.street`.                 |
| BR-008 | The library must support collection validation.                | Lists, sets, and other iterable values can be validated element by element with paths that identify the element location.      |
| BR-009 | The library must support cross-field validation.               | A rule can compare multiple fields, such as requiring `endDate` to be after `startDate`.                                       |
| BR-010 | The library must support custom validators.                    | Users can define reusable domain-specific rules without modifying the library.                                                 |
| BR-011 | Validation results must be structured.                         | Invalid results include field path, error code, human-readable message, and rejected value where available.                    |
| BR-012 | The library must support collect-all validation.               | Users can receive every violation found during a validation run.                                                               |
| BR-013 | The library must support fail-fast validation.                 | Users can configure validation to stop after the first violation.                                                              |
| BR-014 | The library must preserve validated values on success.         | A successful result returns the original valid value.                                                                          |
| BR-015 | The public API must be suitable for DTO validation.            | Basic DTO examples can be expressed without excessive boilerplate or runtime reflection.                                       |

## Should-have business requirements

| ID     | Requirement                                                         | Acceptance criteria                                                                        |
|--------|---------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| BR-101 | The library should support conditional validation.                  | Users can run rules only when a predicate is true, for example `validateIf { isActive }`.  |
| BR-102 | The library should support validation groups.                       | Users can choose scenario-specific rule sets such as `OnCreate` and `OnUpdate`.            |
| BR-103 | The library should support suspend validators.                      | Users can run async checks, such as database uniqueness checks, from coroutine-aware APIs. |
| BR-104 | The library should support annotation-driven validation generation. | KSP can generate validators from annotated data classes.                                   |
| BR-105 | The library should support localization.                            | Messages can be resolved through pluggable message bundles or message resolvers.           |

## Could-have business requirements

| ID     | Requirement                                              | Acceptance criteria                                                                      |
|--------|----------------------------------------------------------|------------------------------------------------------------------------------------------|
| BR-201 | The library could provide a Ktor integration.            | Request bodies can be automatically validated and mapped to HTTP errors.                 |
| BR-202 | The library could provide Spring Boot autoconfiguration. | Spring applications can register and use generated or DSL validators with minimal setup. |
| BR-203 | The library could provide kotlinx.serialization hooks.   | Objects can be validated during or immediately after deserialization.                    |
| BR-204 | The library could provide Compose UI support.            | Compose forms can expose field state and field-level validation errors.                  |
| BR-205 | The library could provide Arrow interop.                 | Validation results can be converted to and from Arrow `Either` or `Validated`.           |
| BR-206 | The library could export schemas.                        | Validation rules can produce OpenAPI or JSON Schema constraint documentation.            |

## Core user journeys

### DSL DTO validation

A developer defines a validator for a DTO, validates an instance, and receives either a valid result
containing the DTO or an invalid result containing structured violations.

### Nested DTO validation

A developer validates a DTO containing nested objects and collections. Violations include complete
paths that identify exactly where each error occurred.

### Cross-field validation

A developer defines a rule that uses more than one field, such as date ordering or mutually
exclusive options. The result identifies the relevant field or object path.

### Custom domain validation

A developer packages a business-specific rule as a reusable validator and applies it across multiple
DTO validators.

### Annotation-based validation

A developer annotates DTO fields and uses generated validators instead of writing the full DSL by
hand.

## Error model requirements

Each validation violation must be able to expose:

| Field          | Requirement                                                                                       |
|----------------|---------------------------------------------------------------------------------------------------|
| Path           | Stable path to the invalid field or object, such as `email`, `address.street`, or `items[0].sku`. |
| Code           | Stable machine-readable error code, such as `not_blank` or `email`.                               |
| Message        | Human-readable default message suitable for developer-facing or simple user-facing output.        |
| Rejected value | The value that failed validation when it is safe and available to expose.                         |

The model should allow future metadata without breaking existing users.

## Validation mode requirements

| Mode        | Requirement                                              |
|-------------|----------------------------------------------------------|
| Collect all | Continue evaluating rules and return all violations.     |
| Fail fast   | Stop evaluating as soon as the first violation is found. |

Collect-all should be the default unless usability testing shows fail-fast is a better default.

## KMP and dependency requirements

1. Core validation APIs must live in shared code.
2. Platform-specific integrations must be separate from the core.
3. The core must not rely on JVM reflection.
4. The core must prefer Kotlin standard library capabilities and KMP-safe APIs.
5. Platform-specific behavior must not leak into common validation semantics.

## Release readiness requirements

The first release is ready when:

1. Must-have requirements BR-001 through BR-015 are implemented.
2. Public DSL examples are documented in the README.
3. Core validators have common tests.
4. Nested validation and error path behavior have tests.
5. Fail-fast and collect-all behavior have tests.
6. The public API has package-level documentation or README examples.
7. A product name has been selected or the release clearly ships under `kotlin-validation`.
