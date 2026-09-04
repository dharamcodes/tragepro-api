# Multi-Agent Engineering Workflow

This document defines the strict, end-to-end engineering workflow, communication protocols, state transitions, and quality gates for **Odin**, **Loki**, and **Thor**.

---

## 🔄 End-to-End Workflow Diagram

```text
+-------------------------------------------------------------+
|                      docs/issues/*.md                       |
|                                                             |
| Feature / Bug / Enhancement Backlog                         |
| Acceptance Criteria & Non-Functional Requirements           |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                          1. ODIN                            |
|                      Staff Engineer                         |
|                                                             |
| • Reads Source Issue                                        |
| • Audits Modulith, MongoDB & Temporal Architecture          |
| • Analyzes System Tradeoffs & Failure Domains               |
| • Authors Feature Design Document                           |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                          2. LOKI                            |
|                           SDE3                              |
|                                                             |
| • Reads Source Issue & Odin's Feature Design                |
| • Authors Low-Level Design (LLD) Document                   |
| • Defines Package Structure, DTOs, Mappings & Interfaces    |
| • Highlights Edge Cases & Implementation Risks              |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                          3. THOR                            |
|                    Software Developer                       |
|                                                             |
| • Reads Issue, Feature Design & LLD                         |
| • Incremental Production Code Implementation                |
| • Unit Tests (Mockito / JUnit 5)                            |
| • Integration Tests (Testcontainers / Modulith)             |
| • Application E2E Tests                                     |
| • Bruno API E2E Tests (bruno/)                              |
| • Quality Checks (Spotless, JaCoCo >= 95%, ./gradlew check) |
| • Authors Issue Coverage Summary                            |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                          4. LOKI                            |
|                           SDE3                              |
|                                                             |
| • Exhaustive Code Quality & Correctness Review              |
| • Validates Acceptance Criteria Traceability                |
| • Reviews Bruno API E2E Scenarios & Security                |
| • Checks Exception Handling, Concurrency & Transactions     |
+-------------------------------------------------------------+
                              |
                     Findings Identified?
                       /             \
                   YES                 NO
                  /                     \
                 v                       v
+----------------------------------+  +----------------------------------+
|             5. THOR              |  |             6. ODIN              |
|        Software Developer        |  |          Staff Engineer          |
|                                  |  |                                  |
| • Fixes BLOCKER / CRITICAL /     |  | • Final Architecture Audit       |
|   MAJOR Review Findings          |  | • Validates System Boundaries    |
| • Re-runs ./gradlew check        |  | • Audits Failure Modes & Metrics |
| • Updates Issue Coverage Summary |  | • Final Sign-Off                 |
+----------------------------------+  +----------------------------------+
                 |                                      |
                 +------------------------------------->|
                                                        v
                                              +-------------------+
                                              | FEATURE APPROVED  |
                                              +-------------------+
```

---

## 📜 Mandatory Workflow Rules

### Rule 1 — Odin Works First on Significant Initiatives
Before any code is written for new features, major enhancements, or architectural refactorings:
- Odin must read the relevant issue in `docs/issues/*.md`.
- Odin must produce a completed [Feature Design Document](odin/feature-design-template.md).
- Implementation must not begin on unvetted architectural designs.

### Rule 2 — Loki Reviews Design & Provides LLD Before Implementation
Loki audits Odin's architecture against low-level maintainability, defining:
- Concrete package layouts within `com.tragepro.api.<module>`.
- Class contracts, MapStruct mappers, and repository query signatures.
- Specific exception hierarchies and input validation constraints.

### Rule 3 — Thor Implements Across All 4 Testing Layers
Thor writes clean, robust code and validates behavior across:
- **Unit Tests**: Domain entities, utilities, and isolated service logic.
- **Integration Tests**: MongoDB queries via Testcontainers and Spring Modulith boundaries.
- **Application E2E Tests**: Full orchestration pipelines.
- **Bruno API E2E Tests**: First-class HTTP requests under `bruno/`.

### Rule 4 — Loki Conducts Exhaustive Code & Test Review
Loki verifies that:
- Every acceptance criterion in the issue has corresponding test assertions.
- Code conforms to Java 25 and Spring Boot 4.1.1 standards.
- Bruno tests are deterministic, isolated, and validate error scenarios.
- All review comments are classified using [Review Severity Guidelines](shared/review-severity.md).

### Rule 5 — Thor Resolves All Non-Trivial Findings
Thor must remediate all findings classified as:
- `BLOCKER`
- `CRITICAL`
- `MAJOR`

Minor suggestions may be addressed or explicitly deferred with documented justification.

### Rule 6 — Odin Grants Final Architectural Approval
Odin conducts the final sign-off to ensure:
- The actual implementation matches the approved system architecture.
- Spring Modulith module boundaries (`@NamedInterface("adapter")`) remain intact.
- Resilience, observability, and failure recovery mechanisms are active.

---

## 🔗 Acceptance Criteria Traceability

Traceability ensures zero requirement drift across the entire engineering lifecycle:

```text
ISSUE (docs/issues/*.md)
   │
   ├── Requirements & Acceptance Criteria
   │
   ▼
FEATURE DESIGN (Odin) & LOW-LEVEL DESIGN (Loki)
   │
   ├── Architectural Components, Classes & API Contracts
   │
   ▼
TEST SUITE (Thor)
   │
   ├── Unit, Integration, Application E2E & Bruno API Tests
   │
   ▼
IMPLEMENTATION (Thor)
   │
   ├── Production Code in com.tragepro.api.*
   │
   ▼
VALIDATION & REVIEW (Loki & Odin)
   │
   └── JaCoCo >= 95%, Spotless Format, Full Acceptance Criteria Verification
```

---

## 📡 Agent Communication & Handoff Protocols

Every handoff between agents must follow a structured contract to ensure clarity and accountability.

---

### 1. Odin → Loki Handoff Protocol

When Odin finishes the high-level architecture:

```markdown
### Odin → Loki Architecture Handoff

* **Source Issue**: `docs/issues/<issue-file>.md`
* **Feature Design Document**: `.agents/designs/<feature-design>.md` (or inline)
* **Target Modules**: `com.tragepro.api.<module>`
* **Architecture Highlights**:
  - Key architectural patterns (e.g., Modulith Adapter, Temporal Workflow, Mongo Collection)
  - Key interfaces and cross-module entry points (`@NamedInterface("adapter")`)
* **Tradeoffs & System Decisions**:
  - Scalability and concurrency considerations
  - Resilience strategies (timeouts, retries, circuit breakers)
* **Testing & Verification Expectations**:
  - Critical end-to-end user journeys to validate
  - Failure modes requiring explicit testing
* **Open Questions / Assumptions**:
  - Documented assumptions for LLD refinement
```

---

### 2. Loki → Thor Handoff Protocol

When Loki provides low-level design guidance to Thor:

```markdown
### Loki → Thor Implementation Handoff

* **Source Issue**: `docs/issues/<issue-file>.md`
* **Feature Design Reference**: `<reference-to-odin-design>`
* **Low-Level Design (LLD)**:
  - Exact package locations (`adapter/`, `web/`, `service/`, `core/`)
  - Class names, records, interfaces, and MapStruct mapper signatures
  - Validation rules (`@NotNull`, `@Size`, `@Valid`, `@Positive`)
  - Exception handling and error response mapping
* **Edge Cases to Guard**:
  - Specific boundary conditions, nullability, duplicate keys, concurrent requests
* **Testing Directives**:
  - Unit test targets and mock strategies
  - Integration test requirements (MongoDB Testcontainers)
  - Bruno API test requirements (`bruno/<module>/<feature>/`)
* **Quality Gate Targets**:
  - Spotless formatting (`./gradlew spotlessApply`)
  - JaCoCo line coverage ($\ge 95\%$)
```

---

### 3. Thor → Loki Handoff Protocol

When Thor submits implementation and tests for review:

```markdown
### Thor → Loki Review Submission

* **Source Issue**: `docs/issues/<issue-file>.md`
* **Summary of Changes**:
  - High-level explanation of code added, modified, or refactored
* **Files Changed**:
  - List of modified and new production and test files
* **Testing Performed**:
  - Unit tests added: `<TestClassNames>`
  - Integration tests added: `<IntegrationTestClassNames>`
  - Application E2E tests added: `<E2ETestClassNames>`
  - Bruno tests added/updated: `bruno/<path>/*.bru`
* **Quality Gate Verification**:
  - `./gradlew spotlessApply`: Passed
  - `./gradlew test`: Passed
  - `./gradlew jacocoTestReport`: Passed (Line Coverage: `<XX.X>%`)
  - `./gradlew check`: Passed
* **Issue Coverage Summary**: (Included as detailed below)
* **Known Limitations / Notes**: Any non-obvious implementation details
```

---

### 4. Loki → Odin Handoff Protocol

When Loki completes code review and forwards for final architecture sign-off:

```markdown
### Loki → Odin Final Sign-off Request

* **Source Issue**: `docs/issues/<issue-file>.md`
* **Feature Design**: `<reference>`
* **Review Summary**:
  - Code correctness, quality, and maintainability: Approved
  - Spring Modulith boundaries respected: Verified
  - Acceptance criteria coverage: $100\%$ verified against tests
  - Bruno API E2E collection: Verified & passing
  - JaCoCo Line Coverage: `<XX.X>%` ($\ge 95\%$)
* **Issues Resolved During Review**:
  - Summary of resolved Blocker / Critical / Major findings
* **Remaining Risks / Operational Notes**:
  - Any runtime, indexing, or configuration notes for production
```

---

## 📊 Issue Coverage Summary Schema

Thor must include this structured matrix in every review submission to guarantee complete acceptance criteria coverage:

```markdown
# Issue Coverage Summary

**Source Issue**: `docs/issues/<issue-file>.md`
**Feature Title**: `<Title>`

---

## Requirement 1: `<Requirement Name>`
* **Status**: `COMPLETE` | `PARTIAL` | `BLOCKED`
* **Implementation**:
  - Class: [`ClassA`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/.../ClassA.java)
  - Method: `processTradeSignal()`
* **Test Verification**:
  - **Unit Tests**: [`ClassATest`](file:///Users/dharam.dev/devspace/tragepro-api/src/test/java/com/tragepro/api/.../ClassATest.java) (`testValidSignal()`, `testInvalidPayload()`)
  - **Integration Tests**: [`ClassAIntegrationTest`](file:///Users/dharam.dev/devspace/tragepro-api/src/test/java/com/tragepro/api/.../ClassAIntegrationTest.java)
  - **Application E2E Tests**: [`StrategyExecutionE2ETest`](file:///Users/dharam.dev/devspace/tragepro-api/src/test/java/com/tragepro/api/.../StrategyExecutionE2ETest.java)
  - **Bruno API Tests**: `bruno/strategy/execute.bru`

---

## Requirement 2: `<Requirement Name>`
* **Status**: `COMPLETE`
* **Implementation**:
  - Class: [`ClassB`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/.../ClassB.java)
* **Test Verification**:
  - **Unit Tests**: [`ClassBTest`](file:///Users/dharam.dev/devspace/tragepro-api/src/test/java/com/tragepro/api/.../ClassBTest.java)
  - **Integration Tests**: [`ClassBIntegrationTest`](file:///Users/dharam.dev/devspace/tragepro-api/src/test/java/com/tragepro/api/.../ClassBIntegrationTest.java)
  - **Bruno API Tests**: `bruno/strategy/status.bru`

---

## Acceptance Criteria Checklist
- [x] **AC-1**: `<Description>` (Verified in `ClassATest#testAC1`)
- [x] **AC-2**: `<Description>` (Verified in `bruno/strategy/execute.bru`)
- [x] **AC-3**: `<Description>` (Verified in `ClassAIntegrationTest#testPersistence`)
```
