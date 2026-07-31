# Phase 4: Expand built-in validation rules

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Done
**Goal:** Add common rules for nullability, strings, numbers, and collections.

This phase grows the DSL with reusable rules that cover typical request/DTO constraints.

## Task 4.1: Add nullability rules

**Status:** Done

Add explicit null-related validators.

Scope:

- Add `notNull`.
- Add `isNull` only if there is a clear use case.
- Ensure nullable property rules do not conflate null with predicate failure unless explicitly
  intended.
- Add tests for null and non-null values.

Expected result:

- Null handling becomes explicit and predictable.

## Task 4.2: Add string length rules

**Status:** Done

Add common string validation helpers.

Scope:

- Add `minLength`.
- Add `maxLength`.
- Add `lengthIn`.
- Support nullable and non-null string properties where appropriate.
- Add tests for boundary values.

Expected result:

- Users do not need custom predicates for basic string length validation.

## Task 4.3: Add numeric comparison rules

**Status:** Done

Add common numeric validation helpers.

Scope:

- Add `min`.
- Add `max`.
- Add `positive` or `nonNegative` if useful.
- Decide whether to support only `Int` first or broader `Comparable` values.
- Add tests for boundary values.

Expected result:

- Numeric validation is more expressive than only `IntRange`.

## Task 4.4: Add collection size rules

**Status:** Done

Add validation helpers for collections.

Scope:

- Add `isNotEmpty`.
- Add `minSize`.
- Add `maxSize`.
- Add `sizeIn`.
- Add tests for empty, boundary, and out-of-range collections.

Expected result:

- Collection validation covers common request/DTO constraints.

## Task 4.5: Rename unclear rule names

**Status:** Done

Improve DSL readability by applying a consistent naming convention.

Chosen convention:

- State assertions use the `is` prefix.
- Possession / collection qualities use the `has` prefix.
- Comparison, size, range, and pattern checks use plain constraint-style verbs.

Renames applied (old names removed):

- `regex` -> `matches`
- `email` -> `isEmail`
- `uuid` -> `isUuid`
- `digitsOnly` -> `isDigitsOnly`
- `lettersOnly` -> `isLettersOnly`
- `alphanumeric` -> `isAlphanumeric`
- `lowercase` -> `isLowercase`
- `uppercase` -> `isUppercase`
- `range` -> `inRange`
- `positive` -> `isPositive`
- `negative` -> `isNegative`
- `nonNegative` -> `isNonNegative`
- `nonPositive` -> `isNonPositive`
- `uniqueElements` -> `hasUniqueElements`

Names kept as-is:

- `isNotNull`, `isNull`, `isNotBlank`, `isNotEmpty`
- `min`, `max`, `minLength`, `maxLength`, `lengthIn`
- `minSize`, `maxSize`, `sizeIn`

Tests updated to use the new names.

Expected result:

- The public DSL reads naturally and has a consistent naming style.

## Task 4.6: Define rules for elements of collection

**Status:** Done

Scope:

- Add `validateEach` for iterable elements and `validateValues` for map values.
- Produce error paths that include the collection index or map key, such as `items[0].name` and
  `items[sku].name`.
- Preserve all element errors during normal validation and return only the first element error in
  fail-fast mode.
- Reuse the nested rule and path composition used by object validation.

Expected result:

- Rules for collection elements are available and produce predictable error paths.

## Task 4.7: Add map validation rules

**Status:** Done

Scope:

- Keep the existing property-level `satisfies` rule as the whole-map predicate.
- Add map size rules: `isNotEmpty`, `minSize`, `maxSize`, and `sizeIn`.
- Add `eachSatisfies` for map values, `validateKeys` for map keys, and `validateEntries` for
  `Map.Entry` values.
- Preserve map paths such as `attributes[sku]`, `attributes[sku].key`, and
  `attributes[sku].value`.
- Treat null maps as valid for map-specific size and traversal helpers, and skip null keys or
  values in key/value traversal. Whole-map `satisfies` keeps predicate-defined null handling.
  Entry validation visits entries with nullable members so callers can validate those members
  explicitly.
- Honor fail-fast mode while traversing map entries and values.

Examples:

```kotlin
val validator = buildValidator {
    Request::attributes.isNotEmpty()
    Request::attributes.eachSatisfies(errorCode = "ATTRIBUTE_BLANK") {
        it.isNotBlank()
    }
    Request::attributes.validateKeys {
        ensure(errorCode = "ATTRIBUTE_KEY") { it.startsWith("x-") }
    }
    Request::attributes.validateEntries {
        Map.Entry<String, String?>::value.isNotNull(errorCode = "ATTRIBUTE_REQUIRED")
    }
}
```

Expected result:

- Maps have first-class size and key/value/entry validation without duplicating whole-map
  `satisfies` behavior.
