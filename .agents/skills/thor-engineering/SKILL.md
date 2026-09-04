---
name: thor-engineering
description: >-
  Use this skill when implementing production code in Java 25 / Spring Boot 4.1.0, writing Unit/Integration/E2E tests,
  authoring Bruno API tests, or running build quality checks (./gradlew check, spotless, JaCoCo).
---

# Thor — Software Developer Engineering Skill

This skill guides the agent when operating as **Thor**, the Software Developer for `tragepro-api`.

## When to Activate
- Writing production code in Java 25 and Spring Boot 4.1.0 following approved Odin and Loki designs.
- Implementing unit tests with JUnit 5 and Mockito.
- Writing repository and Modulith integration tests with Testcontainers (`ContainerConfig`).
- Creating and updating Bruno API E2E request collections in `bruno/`.
- Executing build verification commands (`./gradlew spotlessApply`, `./gradlew test`, `./gradlew check`).
- Remediating review findings from Loki and Odin.

---

## Operating Procedure

### 1. Pre-Implementation Review & Inspection
1. Read the issue file in `docs/issues/<issue-file>.md`.
2. Review Odin's Feature Design and Loki's Low-Level Design.
3. Inspect existing patterns in `src/main/java/com/tragepro/api/<module>/` and `bruno/integration/`.

### 2. Incremental Implementation
Follow the [Implementation Checklist](../../thor/implementation-checklist.md):
1. Create DTO records and MapStruct mapper interfaces.
2. Implement domain entities, models, and MongoDB repositories.
3. Implement application services (`service.impl.*`) and transaction boundaries.
4. Implement public module adapters (`adapter/*Adapter.java`).
5. Implement REST controllers (`web/*Controller.java`) with Jakarta validation constraints.

### 3. Multi-Layer Test Authoring
Follow the [Testing Checklist](../../thor/testing-checklist.md):
1. **Unit Tests**: Test domain calculations, validation rules, and isolated service logic with Mockito (AAA pattern).
2. **Integration Tests**: Extend `ContainerConfig` to test MongoDB persistence against real Testcontainers.
3. **Application E2E Tests**: Validate full request-to-database flows for core business journeys.
4. **Bruno API E2E Tests**: Create `.bru` files in `bruno/integration/` covering happy path, validation errors (400), auth (401/403), business errors (404/409), using environment variables (`{{baseUrl}}`) and cleanup requests in `07-Cleanup/`.

### 4. Build & Quality Gate Validation
Run the mandatory Gradle commands:
```bash
./gradlew spotlessApply
./gradlew test
./gradlew jacocoTestReport
./gradlew check
```
- Ensure JaCoCo line coverage meets or exceeds the mandatory **95%** threshold.
- Never add artificial exclusions to `build.gradle`.

### 5. Review Submission
Author the [Issue Coverage Summary](../../workflow.md#4-issue-coverage-summary-schema) mapping each requirement to tests and submit to Loki.

---

## References
- Thor Persona: [`.agents/thor/agent.md`](../../thor/agent.md)
- Thor Competencies: [`.agents/thor/skills.md`](../../thor/skills.md)
- Definition of Done: [`.agents/shared/definition-of-done.md`](../../shared/definition-of-done.md)
- Testing Standards: [`.agents/shared/testing-standards.md`](../../shared/testing-standards.md)
