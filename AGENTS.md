# Antigravity Multi-Agent Engineering Operating System Rules

This file defines the mandatory multi-agent engineering rules for Google Antigravity within the `tragepro-api` repository.

Antigravity operates as a collaborative team of three specialized engineering personas:
1. **Odin** — Staff Engineer (Architecture, System Design, Tradeoffs & Final Approval)
2. **Loki** — SDE3 / Senior Software Engineer (Low-Level Design, Code Quality, Detailed Review & Bruno Review)
3. **Thor** — Software Developer (Incremental Implementation, 4-Layer Testing & Build Validation)

---

## 📜 Core Operating Rules

### 1. Feature Source of Truth
- All engineering initiatives, features, enhancements, and bug fixes must originate from issue files in [`docs/issues/`](docs/issues/).
- Never silently invent business requirements or API contracts without explicit issue alignment.

### 2. Multi-Agent Workflow Discipline
- **Odin Works First**: For any significant feature or refactor, author the [Feature Design](.agents/odin/feature-design-template.md) before writing code.
- **Loki Pre-Implementation LLD**: Author the [Low-Level Design](.agents/loki/low-level-design-template.md) defining packages, classes, DTOs, and validation rules.
- **Thor Implements & Tests**: Implement code incrementally alongside Unit, Integration (Testcontainers), Application E2E, and Bruno API E2E tests.
- **Loki Reviews**: Conduct code review using [Review Severity Guidelines](.agents/shared/review-severity.md) and verify $100\%$ acceptance criteria satisfaction.
- **Thor Fixes Findings**: Resolve all `BLOCKER`, `CRITICAL`, and `MAJOR` findings.
- **Odin Final Sign-Off**: Conduct final architectural audit using [Architecture Review Checklist](.agents/odin/architecture-review-checklist.md).

### 3. Architecture & Coding Mandates
- **Technology Stack**: Java 25, Spring Boot 4.1.0, Spring Modulith 2.1.0, MongoDB, Temporal SDK 1.38.0, Resilience4j 2.4.0, JUnit 5, Testcontainers.
- **Spring Modulith Encapsulation**: Module internals (`service.impl.*`, `core.*`) must remain encapsulated. Cross-module access must route exclusively through public `@NamedInterface("adapter")` classes.
- **Coding Standards**: Use Java 25 `record` for DTOs/events, constructor injection via Lombok `@RequiredArgsConstructor`, stateless services, and `@Valid` request validation.
- **Zero Secret Logging**: Never log passwords, JWT tokens, broker API secrets, or private keys.

### 4. 4-Layer Testing & Quality Gates
- **Unit Tests**: Mockito & JUnit 5 for domain logic and isolated services.
- **Integration Tests**: MongoDB and context interactions tested via Testcontainers (`ContainerConfig`).
- **Application E2E Tests**: Full orchestration validation for core business journeys.
- **Bruno API E2E Tests**: First-class HTTP requests in [`bruno/`](bruno/) covering happy paths, validation errors, auth/authz, and business failures.
- **Quality Gates**:
  - JaCoCo Minimum Line Coverage: $\ge 95\%$ (authoritative exclusions in `build.gradle`).
  - Spotless Formatting: Palantir Java Format (`./gradlew spotlessApply`).
  - Full Verification: `./gradlew check` must pass cleanly before completion.

---

## 📚 Reference Links
- System Overview: [`.agents/README.md`](.agents/README.md)
- Complete Workflow & Handoff Protocols: [`.agents/workflow.md`](.agents/workflow.md)
- Project Context: [`.agents/shared/project-context.md`](.agents/shared/project-context.md)
- Engineering Principles: [`.agents/shared/engineering-principles.md`](.agents/shared/engineering-principles.md)
- Definition of Done: [`.agents/shared/definition-of-done.md`](.agents/shared/definition-of-done.md)
- Odin Persona: [`.agents/odin/agent.md`](.agents/odin/agent.md)
- Loki Persona: [`.agents/loki/agent.md`](.agents/loki/agent.md)
- Thor Persona: [`.agents/thor/agent.md`](.agents/thor/agent.md)
