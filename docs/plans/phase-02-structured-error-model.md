# Phase 2: Structured Error Model

## Status

Completed: 0 / 6 tasks, 0%.

## Tasks

| Status  | ID     | Task                                                                  | Files likely touched                          | Done when                                                                      |
|---------|--------|-----------------------------------------------------------------------|-----------------------------------------------|--------------------------------------------------------------------------------|
| Pending | P2-001 | Replace message-only errors with structured `ValidationError` fields. | `ValidationError.kt`, platform actuals, tests | Error has path, code, message, and rejected value fields.                      |
| Pending | P2-002 | Preserve `reasons()` compatibility.                                   | `ValidationResult.kt`, tests                  | Existing callers can still get message lists.                                  |
| Pending | P2-003 | Add factory helpers for common error creation.                        | `ValidationError.kt` or new helper file       | Tests can create errors without repeating default values.                      |
| Pending | P2-004 | Define default error code constants.                                  | New constants file                            | Built-in validators can reuse stable codes.                                    |
| Pending | P2-005 | Test rejected value handling.                                         | Common tests                                  | Invalid results expose rejected values when provided.                          |
| Pending | P2-006 | Test empty path handling for object-level errors.                     | Common tests                                  | Cross-field or object-level errors can use an empty or root path consistently. |

