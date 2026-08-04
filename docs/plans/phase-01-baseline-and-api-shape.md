# Phase 1: Baseline and API Shape

## Status

Completed: 3 / 5 tasks, 60%.

## Tasks

| Status  | ID     | Task                                                             | Files likely touched                   | Done when                                                                                 |
|---------|--------|------------------------------------------------------------------|----------------------------------------|-------------------------------------------------------------------------------------------|
| Done    | P1-001 | Capture current public API behavior in tests before refactoring. | `src/commonTest/.../ValidationSpec.kt` | Existing `validate`, `ensure`, `ensureNotNull`, and `ensureNotBlank` behavior is covered. |
| Done    | P1-002 | Add a small README example for the current API.                  | `README.md`                            | README shows a minimal DTO validation example.                                            |
| Pending | P1-003 | Decide and document package-level API naming conventions.        | `README.md` or source KDoc             | Naming for validators, result types, and error codes is consistent.                       |
| Pending | P1-004 | Introduce a `ValidationMode` enum.                               | `src/commonMain/...` and tests         | `COLLECT_ALL` and `FAIL_FAST` are defined but not yet deeply wired.                       |
| Done    | P1-005 | Add tests describing collect-all as the default mode.            | `src/commonTest/...`                   | Current default behavior is protected by tests.                                           |

