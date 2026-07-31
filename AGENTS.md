# AGENTS.md

- Apply changes according to [plan](./docs/IMPLEMENTATION_INDEX.md), one task (not phase) at a time.
- Update docs when task is done.
- Use Test-Driven Development (TDD) - write tests, wait for my approval, then implement.
- Test only public APIs, not internal implementation details.
- Prefer functional programming style; Public APIs should be built around pure functions, immutable
  values, function composition, and explicit data transformations. Mutable builders may still be
  used internally for Kotlin DSL ergonomics, but the resulting validators should be immutable and
  reusable.
- After each change, format code by `./gradlew ktlintFormat`.
