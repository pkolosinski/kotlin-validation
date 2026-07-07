# Kotlin Validation Implementation Plan

This index links to small implementation plans. Each phase is split into tasks intended to fit in a
single small-model context window.

## Progress summary

Progress is based on the current repository state and the task-level "Done when" criteria in each
phase file.

| Phase     | Plan                                                                                     |  Completed | Progress |
|-----------|------------------------------------------------------------------------------------------|-----------:|---------:|
| 1         | [Baseline and API shape](plans/phase-01-baseline-and-api-shape.md)                       |      2 / 5 |      40% |
| 2         | [Structured error model](plans/phase-02-structured-error-model.md)                       |      0 / 6 |       0% |
| 3         | [Validation context internals](plans/phase-03-validation-context-internals.md)           |      0 / 6 |       0% |
| 4         | [Field-based DSL](plans/phase-04-field-based-dsl.md)                                     |      0 / 6 |       0% |
| 5         | [Basic built-in validators](plans/phase-05-basic-built-in-validators.md)                 |      0 / 7 |       0% |
| 6         | [Numeric and comparable validators](plans/phase-06-numeric-and-comparable-validators.md) |      0 / 5 |       0% |
| 7         | [Null-safety API](plans/phase-07-null-safety-api.md)                                     |      0 / 5 |       0% |
| 8         | [Custom validators](plans/phase-08-custom-validators.md)                                 |      0 / 5 |       0% |
| 9         | [Cross-field validation](plans/phase-09-cross-field-validation.md)                       |      0 / 4 |       0% |
| 10        | [Nested object validation](plans/phase-10-nested-object-validation.md)                   |      0 / 6 |       0% |
| 11        | [Collection validation](plans/phase-11-collection-validation.md)                         |      0 / 6 |       0% |
| 12        | [Conditional validation](plans/phase-12-conditional-validation.md)                       |      0 / 5 |       0% |
| 13        | [Validation groups](plans/phase-13-validation-groups.md)                                 |      0 / 5 |       0% |
| 14        | [Suspend validators](plans/phase-14-suspend-validators.md)                               |      0 / 5 |       0% |
| 15        | [KSP annotations](plans/phase-15-ksp-annotations.md)                                     |      0 / 7 |       0% |
| 16        | [Localization](plans/phase-16-localization.md)                                           |      0 / 5 |       0% |
| 17        | [Integrations backlog](plans/phase-17-integrations-backlog.md)                           |      0 / 9 |       0% |
| **Total** |                                                                                          | **2 / 97** |   **2%** |

## Ground rules for every task

1. Keep changes scoped to the task.
2. Prefer `commonMain` for core behavior.
3. Add or update focused tests for changed behavior.
4. Update README examples only when the public API changes.
5. Preserve existing behavior unless the task explicitly changes it.

## Suggested MVP cut

The first MVP should include phases 1 through 11. That delivers the core KMP DSL, structured errors,
built-in validators, fail-fast and collect-all modes, custom validators, cross-field validation,
nested validation, and collection validation.

Phases 12 through 16 should follow once the core API feels stable. Phase 17 should stay separate
because integrations can introduce platform-specific dependencies and module complexity.

