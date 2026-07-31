# Phase 6: Improve documentation and examples

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Not started  
**Goal:** Make the README a complete getting-started guide for the library.

This phase documents installation, common usage, and advanced scenarios.

## Task 6.1: Expand README installation section

Document how consumers add the library.

Scope:
- Add Gradle Kotlin DSL dependency example.
- Add repository configuration if GitHub Packages is the only publishing target.
- Document supported targets.

Expected result:
- New users can install the library without inspecting build files.

## Task 6.2: Add basic usage examples

Document common validation flows.

Scope:
- Show reusable validator creation.
- Show field-level rules.
- Show object-level rules.
- Show result handling.

Expected result:
- README demonstrates the intended DSL clearly.

## Task 6.3: Add nested and collection examples

Document the richer validation scenarios once implemented.

Scope:
- Show nested object validation.
- Show collection item validation.
- Show resulting error paths.

Expected result:
- Users understand how paths are represented and how to validate realistic DTOs.
