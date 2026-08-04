# Phase 5: Improve public API structure

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Done
**Goal:** Organize source files and visibility to expose a clean, intentional API.

This phase focuses on codebase structure rather than new features. Existing validation behavior,
error paths, null handling, and fail-fast semantics are preserved.

## Task 5.1: Split source files by functional responsibility

**Status:** Done

Move related declarations into focused files.

Scope:
- Keep `ValidationResult` in `ValidationResult.kt`.
- Move `ValidationError` to `ValidationError.kt`.
- Move functional validator type aliases, factories, and composition helpers to `Validator.kt`.
- Keep DSL construction implementation in `ValidationBuilder.kt`.
- Keep built-in rules in files grouped by domain, for example `StringRules.kt`, `NumberRules.kt`, `CollectionRules.kt`.
- Keep pure result transformation and callback helpers in `ValidationResultOps.kt`.

Expected result:
- The codebase is easier to navigate as features grow.

## Task 5.2: Review visibility modifiers

**Status:** Done

Make the public API intentional.

Scope:
- Decide which types and functions are public library API.
- Keep `Validator<T>` as the public function-type abstraction.
- Keep `ValidationBuilder` public as the DSL receiver while retaining its internal constructor.
- Keep rule storage, path composition, and result-combination helpers internal or private.
- Add comments only where public API behavior is not obvious.

Expected result:
- Consumers get a clean API surface and internals remain changeable.

## Task 5.3: Minimize object-oriented extension points

**Status:** Done

Review the API for unnecessary classes, interfaces, or inheritance-shaped abstractions.

Scope:
- Use `typealias Validator<T>` as the primary validator abstraction.
- Avoid adding abstract base classes or inheritance hierarchies.
- Prefer top-level functions, extension functions, immutable data classes, and sealed result types.
- Keep `ValidationBuilder` only for DSL ergonomics and snapshot its rules when building a validator.

Expected result:
- The public API stays functional, small, and easy to compose.

## Task 5.4: Consider explicit API mode

**Status:** Done

Enable Kotlin explicit API mode for the published library.

Scope:
- Enable strict `explicitApi()` mode.
- Add explicit public visibility and return types across the common public API and built-in rules.

Expected result:
- Public API changes become deliberate and reviewable.

## Completion checks

- Public API tests cover explicit validator and DSL receiver usage, empty and identity composition,
  validator reuse, composition ordering, and `ValidationError` defaults.
- Existing result, rule, nested, collection, map, and fail-fast tests remain passing.
- `./gradlew jvmTest ktlintCheck` passes.
