# Kotlin Validation Library — Brainstorm Session

**Date:** 2026-07-02

---

## 1. Library Name Proposals

| Name           | Concept                  | Notes                                                      |
|----------------|--------------------------|------------------------------------------------------------|
| **Kward**      | Kotlin + Guard           | Sleek, punchy, modern — signals protection and type-safety |
| **Valiko**     | Validation + Kotlin      | Melodic, memorable, strong open-source brand               |
| **Veriko**     | Verify + Kotlin          | Professional, enterprise-ready, trustworthy                |
| **Kalliper**   | Caliper (precision tool) | Technical name evoking exact measurement and calibration   |
| **Kotilidate** | Kotlin + Validate        | Playful, direct blend — great SEO and searchability        |

---

## 2. Core Concept

A **Kotlin Multiplatform (KMP)** library for validating data, featuring:

- **DSL** — declarative DTO validation with pleasure
- **Annotations** — Spring-style annotation-based validation

---

## 3. Feature Analysis

### 🔴 Must-Have

#### Core Rules Engine

- Built-in validators: `notNull`, `notBlank`, `minLength`, `maxLength`, `min`, `max`, `regex`,
  `email`, `url`, `range`
- **Composable/nested validation** — validate nested objects & collections recursively with proper
  field path tracking (e.g. `address.street`)
- **Rich error model** — structured result with field path, error code, human-readable message, and
  rejected value
- **Fail-fast vs collect-all** — stop at first error OR aggregate all violations
- **Cross-field validation** — rules spanning multiple fields (e.g. `endDate > startDate`)
- **Custom validators** — simple extension points for domain-specific rules
- **Null-safety awareness** — leverage Kotlin's type system; separate `validateNullable` vs non-null
  chains
- **KMP-clean core** — zero platform-specific deps in `commonMain`

---

### 🟡 Nice-to-Have

#### Power Features

- **Conditional validation** — `validateIf { isActive }` blocks
- **Validation groups** — `OnCreate`, `OnUpdate` scenarios (like JSR-380)
- **Suspend/async validators** — for DB uniqueness checks etc.
- **KSP code generation** — derive validators from `@Validate` annotated data classes

#### Integrations

- **Ktor plugin** — auto-validate request bodies
- **Spring Boot autoconfigure** — play nicely with existing Spring world
- **kotlinx.serialization hooks** — validate during deserialization
- **Compose UI support** — form field state + error display

#### DX & Ecosystem

- **Arrow `Either`/`Validated` interop** — for functional programmers
- **Localization** — pluggable message bundles (i18n)
- **OpenAPI/JSON Schema export** — generate constraint docs from validators

---

## 4. Key Differentiator

> **Full KMP support + idiomatic Kotlin DSL with type-safe field references
using `KProperty` (`::email`)**
>
> This is the killer angle vs Bean Validation — market this hard.

---
