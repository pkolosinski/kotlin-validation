# Phase 5: Improve public API structure

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Not started
**Goal:** Organize source files and visibility to expose a clean, intentional API.

This phase focuses on codebase structure rather than new features.

## Task 5.1: Split source files by functional responsibility

Move related declarations into focused files.

Scope:
- Keep `ValidationResult` in `ValidationResult.kt`.
- Move `ValidationError` to `ValidationError.kt`.
- Move functional validator type aliases, factories, and composition helpers to `Validator.kt`.
- Keep DSL construction implementation in `ValidationBuilder.kt`.
- Keep built-in rules in files grouped by domain, for example `StringRules.kt`, `NumberRules.kt`, `CollectionRules.kt`.
- Keep pure transformation helpers in focused files, for example `ValidationResultOps.kt` or `ValidationErrorOps.kt`.

Expected result:
- The codebase is easier to navigate as features grow.

## Task 5.2: Review visibility modifiers

Make the public API intentional.

Scope:
- Decide which types and functions are public library API.
- Keep implementation helpers internal or private.
- Avoid exposing type aliases if they are not meant for consumers.
- Avoid exposing mutable builder details where pure validator functions are sufficient.
- Add comments only where public API behavior is not obvious.

Expected result:
- Consumers get a clean API surface and internals remain changeable.

## Task 5.3: Minimize object-oriented extension points

Review the API for unnecessary classes, interfaces, or inheritance-shaped abstractions.

Scope:
- Use `typealias Validator<T>` as the primary validator abstraction.
- Avoid adding abstract base classes or inheritance hierarchies.
- Prefer top-level functions, extension functions, immutable data classes, and sealed result types.
- Keep classes only where they model data or are needed for DSL ergonomics.

Expected result:
- The public API stays functional, small, and easy to compose.

## Task 5.4: Consider explicit API mode

Evaluate adding Kotlin explicit API mode.

Scope:
- Enable explicit API mode if the project is intended as a published library.
- Add explicit visibility and return types where required.
- Keep this task separate because it may touch many declarations.

Expected result:
- Public API changes become deliberate and reviewable.
