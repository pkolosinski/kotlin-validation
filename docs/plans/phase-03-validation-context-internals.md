# Phase 3: Validation Context Internals

## Status

Completed: 0 / 6 tasks, 0%.

## Tasks

| Status | ID | Task | Files likely touched | Done when |
|---|---|---|---|---|
| Pending | P3-001 | Replace `typealias ValidationState` with a real validation context class. | `Validation.kt`, tests | Context owns errors and mode. |
| Pending | P3-002 | Add an internal `addError` function to the context. | `Validation.kt` | Error addition is centralized. |
| Pending | P3-003 | Wire `ValidationMode.COLLECT_ALL` into `validate`. | `Validation.kt`, tests | Collect-all behavior remains unchanged. |
| Pending | P3-004 | Wire `ValidationMode.FAIL_FAST` into `validate`. | `Validation.kt`, tests | Validation stops after the first error. |
| Pending | P3-005 | Add context state for the current field path. | `Validation.kt`, tests | Nested blocks can push and pop path segments. |
| Pending | P3-006 | Add tests proving path state does not leak between fields. | Common tests | Errors after nested validation have correct independent paths. |

