# Phase 7: Library quality and release hardening

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Not started  
**Goal:** Add documentation generation and binary compatibility validation before release.

This phase hardens the library for publication.

## Task 7.1: Add Dokka documentation

Generate API documentation for public declarations.

Scope:
- Add Dokka plugin.
- Document key public API entry points.
- Add a documentation generation task to local verification.

Expected result:
- Published API is easier to discover and understand.

## Task 7.2: Add binary compatibility validation

Protect public API compatibility.

Scope:
- Add Kotlin binary compatibility validator.
- Generate the initial API baseline.
- Document how to update the baseline intentionally.

Expected result:
- Breaking API changes are visible during review.
