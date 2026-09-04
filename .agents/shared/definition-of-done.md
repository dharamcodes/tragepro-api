# Definition of Done (DoD)

A feature, bug fix, or architectural enhancement is considered **Done** and ready for production deployment only when every item in this checklist is fully satisfied and verified.

---

## ✅ Definitive Definition of Done Checklist

### 1. Requirements & Design Alignment
- [ ] A corresponding issue file exists in `docs/issues/*.md`.
- [ ] **Odin** read the source issue and produced a comprehensive [Feature Design](../odin/feature-design-template.md).
- [ ] The Feature Design document explicitly references the source issue, assumptions, and non-goals.
- [ ] **Loki** read the source issue and provided a [Low-Level Design (LLD)](../loki/low-level-design-template.md) covering package layouts, class responsibilities, and validation.
- [ ] **Thor** reviewed and confirmed understanding of the issue, Feature Design, and LLD before implementing.

---

### 2. Implementation & Code Quality
- [ ] All functional requirements specified in the issue are implemented.
- [ ] All acceptance criteria in the issue are completely satisfied.
- [ ] Code strictly follows [Coding Standards](coding-standards.md) (Java 25 records, constructor injection, stateless singletons, centralized exceptions).
- [ ] Spring Modulith encapsulation boundaries are preserved; cross-module communication uses `@NamedInterface("adapter")`.
- [ ] Sensitive data (passwords, JWT secrets, broker API keys) is never logged or exposed in responses.
- [ ] MongoDB queries are optimized with appropriate indexes; no unindexed collection scans.
- [ ] External client integrations implement Resilience4j fault tolerance (timeouts, retries with jitter, circuit breakers).
- [ ] Temporal workflows maintain strict determinism; non-deterministic operations reside solely in activities.

---

### 3. Testing & Verification
- [ ] **Unit Tests**: Domain entities, calculation algorithms, and isolated service logic have comprehensive unit tests with Mockito and JUnit 5.
- [ ] **Integration Tests**: MongoDB queries and Spring context slices are tested using Testcontainers (`ContainerConfig`).
- [ ] **Spring Modulith Verification**: `ApplicationModules.of(Application.class).verify()` passes.
- [ ] **Application E2E Tests**: Critical end-to-end user journeys are verified across internal layers.
- [ ] **Bruno API E2E Tests**:
  - [ ] New relevant endpoints have corresponding `.bru` test files in `bruno/`.
  - [ ] Modified endpoints have updated Bruno tests.
  - [ ] Happy path (2xx) responses and payload schemas are validated.
  - [ ] Input validation errors (400) are asserted.
  - [ ] Authentication (401) and authorization (403) scenarios are tested where applicable.
  - [ ] Business error states (404, 409, 422) are tested.
  - [ ] Test fixtures and data setup/cleanup are handled safely.
  - [ ] No hardcoded secrets or production credentials are committed to Bruno files.

---

### 4. Build, Formatting & Quality Gates
- [ ] `./gradlew spotlessApply` was executed and all files conform to Spotless Palantir formatting rules.
- [ ] `./gradlew test` executes cleanly with zero failures.
- [ ] `./gradlew jacocoTestReport` completes successfully.
- [ ] **JaCoCo Minimum Line Coverage**: Code coverage achieves $\ge 95\%$ line coverage without artificial exclusions.
- [ ] `./gradlew check` passes completely.

---

### 5. Multi-Agent Review Sign-Offs
- [ ] **Thor** provided a completed [Issue Coverage Summary](../workflow.md#4-issue-coverage-summary-schema) mapping requirements to tests.
- [ ] **Loki** conducted code and test review, approving code quality, correctness, and Bruno API coverage.
- [ ] **Loki** verified that $100\%$ of acceptance criteria are satisfied.
- [ ] **Odin** conducted final architecture review, approving system integrity and non-functional requirements.
- [ ] **Zero Findings Remaining**:
  - `0` BLOCKER findings
  - `0` CRITICAL findings
  - `0` Unresolved MAJOR findings
