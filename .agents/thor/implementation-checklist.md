# Thor — Implementation Workflow Checklist

This checklist guides **Thor** step-by-step through the implementation of a feature or bug fix.

---

## 📖 Step 1: Read & Understand
- [ ] Read the primary issue file in `docs/issues/<issue-file>.md`.
- [ ] Read Odin's [Feature Design](../odin/feature-design-template.md) for architectural context and tradeoffs.
- [ ] Read Loki's [Low-Level Design](../loki/low-level-design-template.md) for package layouts, class responsibilities, and validation rules.
- [ ] Verify that all acceptance criteria and edge cases are clearly understood.

---

## 🔍 Step 2: Inspect Existing Repository Patterns
- [ ] Inspect existing classes in `com.tragepro.api.<module>/`.
- [ ] Inspect existing public adapters (`@NamedInterface("adapter")`).
- [ ] Inspect existing repository tests extending `ContainerConfig`.
- [ ] Inspect existing Bruno API requests in `bruno/integration/`.
- [ ] Ensure the planned implementation follows existing repository conventions.

---

## 💻 Step 3: Implement Incrementally
- [ ] Implement DTO records and MapStruct mappers first.
- [ ] Implement core domain models, calculation engines, and MongoDB repositories.
- [ ] Implement application services (`service.impl.*`) and transaction boundaries.
- [ ] Implement public module adapters (`adapter/*Adapter.java`).
- [ ] Implement REST controllers (`web/*Controller.java`) with `@Valid` constraints.
- [ ] Ensure Spring Modulith module boundaries are strictly respected.
- [ ] Ensure zero secrets or sensitive credentials are hardcoded or logged.

---

## 🧪 Step 4: Develop Multi-Layer Test Suites
- [ ] **Unit Tests**: Write isolated unit tests for domain logic and service branches.
- [ ] **Integration Tests**: Write repository and Modulith integration tests using Testcontainers.
- [ ] **Application E2E Tests**: Test critical end-to-end user workflows.
- [ ] **Bruno API E2E Tests**:
  - [ ] Create/update `.bru` files for all modified or new REST endpoints.
  - [ ] Cover happy path (2xx), validation errors (400), auth failures (401/403), and business errors (404/409).
  - [ ] Ensure Bruno tests use environment variables (`{{baseUrl}}`) and include cleanup in `07-Cleanup/`.

---

## ⚙️ Step 5: Build & Quality Gate Validation
- [ ] Run `./gradlew spotlessApply` to format all modified files.
- [ ] Run `./gradlew test` to execute all unit and integration tests.
- [ ] Run `./gradlew jacocoTestReport` and verify line coverage achieves $\ge 95\%$.
- [ ] Run `./gradlew check` to verify full compilation, Spotless, and JaCoCo quality gates.

---

## 📤 Step 6: Review Preparation & Handoff
- [ ] Author the [Issue Coverage Summary](../workflow.md#4-issue-coverage-summary-schema) mapping each requirement and acceptance criterion to code and tests.
- [ ] Submit the review package to **Loki** following the Thor $\to$ Loki handoff protocol.
- [ ] Address all review findings promptly.
