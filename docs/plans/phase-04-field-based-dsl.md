# Phase 4: Field-Based DSL

## Status

Completed: 0 / 6 tasks, 0%.

## Tasks

| Status | ID | Task | Files likely touched | Done when |
|---|---|---|---|---|
| Pending | P4-001 | Add a `field(KProperty1<T, V>)` DSL entry point. | New DSL file, tests | A rule can be attached to `Dto::field`. |
| Pending | P4-002 | Add a field scope that exposes the field value. | DSL file, tests | Field validators can read the current value. |
| Pending | P4-003 | Ensure field errors include the property name as path. | DSL file, tests | `User::email` produces path `email`. |
| Pending | P4-004 | Support custom field-level messages. | DSL file, tests | A rule can override the default message. |
| Pending | P4-005 | Support custom field-level error codes. | DSL file, tests | A rule can override the default code. |
| Pending | P4-006 | Document the field DSL in README. | `README.md` | README shows `field(User::email)` usage. |

