# Loki — Code & Test Review Checklist

This checklist is executed by **Loki** during detailed code and test reviews.

---

## 🎯 1. Requirements & Acceptance Criteria Fulfillment
- [ ] **Issue Traceability**: Every requirement and acceptance criterion in `docs/issues/*.md` is implemented and verified.
- [ ] **Boundary Conditions**: Null inputs, empty collections, zero/negative amounts, and extreme values are safely handled.
- [ ] **Error Responses**: Appropriate HTTP status codes (400, 401, 403, 404, 409, 500) and structured error payloads are returned.

---

## ☕ 2. Java 25 & Spring Boot Standards
- [ ] **Java 25 Idioms**: DTOs and event payloads use immutable `record` definitions; pattern matching is used where appropriate.
- [ ] **Dependency Injection**: Spring components use constructor injection via Lombok `@RequiredArgsConstructor`; zero `@Autowired` field injection.
- [ ] **Stateless Services**: Spring beans contain zero mutable instance state.
- [ ] **Validation**: Controller endpoints enforce `@Valid` or `@Validated` on request bodies and path variables.
- [ ] **Exception Handling**: No empty `catch` blocks; no raw `catch (Exception e)` without proper rethrow or structured handling.
- [ ] **DTO Mapping**: MapStruct mappers handle all entity-to-DTO and DTO-to-entity conversions; zero entity leaking.

---

## 📦 3. Spring Modulith & Modular Encapsulation
- [ ] **Module Boundaries**: Internal implementation classes (`service.impl.*`, `core.*`) are not directly imported across modules.
- [ ] **Adapter Facades**: Cross-module calls route strictly through `@NamedInterface("adapter")` classes.
- [ ] **Zero Circular Dependencies**: Module dependencies remain strictly acyclic.

---

## 🍃 4. MongoDB Persistence & Data Access
- [ ] **Indexing**: Queries use supported indexes; no unindexed collection scans.
- [ ] **Query Safety**: Queries use type-safe `Query`/`Criteria` or Spring Data repository methods; no dynamic string concatenation.
- [ ] **Projections**: Heavy queries load only necessary fields via projection interfaces or records.

---

## ⏱ 5. Temporal & Resilience4j Patterns
- [ ] **Workflow Determinism**: Temporal workflows contain zero non-deterministic calls (no random numbers, direct clocks, or I/O).
- [ ] **Activity Retry & Timeouts**: Activities configure explicit timeout thresholds and exponential retry policies.
- [ ] **Fault Tolerance**: Network-facing clients configure Resilience4j circuit breakers, timeouts, and rate limiters.

---

## 🧪 6. Test Suite Quality & Coverage
- [ ] **Unit Tests**: Domain logic, algorithms, and service branches are isolated with Mockito and JUnit 5.
- [ ] **Integration Tests**: MongoDB and Spring context interactions are tested using Testcontainers (`ContainerConfig`).
- [ ] **JaCoCo Coverage**: Code changes meet or exceed the mandatory $95\%$ line coverage threshold.
- [ ] **Deterministic Tests**: Zero `Thread.sleep()` in test methods; asynchronous tests use `Awaitility`.

---

## 🌐 7. Bruno API E2E Test Review
- [ ] **Endpoint Coverage**: All new or modified endpoints have corresponding `.bru` requests in `bruno/`.
- [ ] **Scenario Coverage**:
  - [ ] Happy path (2xx) responses assert expected JSON structure and fields.
  - [ ] Validation errors (400) assert error messages and problem details.
  - [ ] Unauthenticated requests (401) and unauthorized requests (403) are tested.
  - [ ] Business conflict / not found scenarios (404, 409) are tested.
- [ ] **Hygiene & Safety**:
  - [ ] Environment variables (`{{baseUrl}}`, `{{accessToken}}`) are used; zero hardcoded secrets.
  - [ ] Test fixtures are cleaned up in `07-Cleanup/`.
  - [ ] Tests execute independently without fragile ordering dependencies.

---

## 🏷 8. Review Severity Assignment
Loki classifies all findings using standard severity tags:
- `BLOCKER`: Must be resolved immediately before merge.
- `CRITICAL`: Must be resolved before review approval.
- `MAJOR`: Must be resolved before review approval.
- `MINOR`: Discretionary improvement.
- `SUGGESTION`: Optional improvement.
