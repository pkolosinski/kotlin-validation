# Deferred: CI and build cleanup

← [Back to implementation index](./IMPLEMENTATION_INDEX.md)

**Status:** Not started  
**Goal:** Resolve build environment and CI concerns after the implementation phases are complete.

These items are intentionally deferred because CI issues were excluded from the immediate implementation scope.

## Task 8.1: Revisit Java toolchain version

Review whether Java 25 is necessary.

Scope:
- Consider using a stable LTS JDK for the Gradle toolchain.
- If Java 25 remains required, configure toolchain provisioning or document the requirement.

Expected result:
- Local and CI builds do not fail solely because the requested JDK is unavailable.

## Task 8.2: Make Android build requirements explicit

Clarify Android SDK requirements.

Scope:
- Document required Android SDK setup.
- Consider whether Android should be part of the default local build.
- Consider adding separate JVM-only verification tasks for fast local feedback.

Expected result:
- Contributors can run meaningful checks without guessing environment requirements.

## Task 8.3: Remove unused build plugins

Clean up build configuration.

Scope:
- Remove KSP if no processor or generated code is used.
- Review unused dependencies in the version catalog.

Expected result:
- Build configuration reflects actual project needs.

## Task 8.4: Harden release workflow

Review GitHub Actions release behavior.

Scope:
- Use full checkout history if Axion release needs tags.
- Review release bump detection for conventional commits and breaking changes.
- Consider separating build, release, and publish permissions more tightly.

Expected result:
- Releases are predictable and less likely to fail because of shallow history or ambiguous commit parsing.
