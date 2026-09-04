# Multi-Agent Engineering Operating System (`.agents`)

Welcome to the **TragePro Multi-Agent Engineering Operating System**. This framework establishes an autonomous, high-rigor pair-and-review engineering system tailored specifically for the `tragepro-api` codebase.

The system orchestrates three specialized agent personas with distinct levels of engineering ownership, collaborating through structured handoffs, rigorous quality gates, and end-to-end traceability.

---

## 🏛 Agent Hierarchy & Ownership

```text
                 ODIN
          Staff Engineer
       Architecture & Design
                  |
                  v
                 LOKI
                  SDE3
        Low-Level Design & Review
                  |
                  v
                 THOR
           Software Developer
      Implementation & Testing
```

While the structural hierarchy guides handoffs, **collaboration remains bidirectional**:
- **Thor** can flag implementation complexities, runtime friction, or performance bottlenecks back to Loki and Odin.
- **Loki** can challenge architectural assumptions or request design revisions from Odin.
- **Odin** serves as the final technical authority on system architecture and cross-cutting tradeoffs.

---

## 👥 The Three Engineering Personas

### 1. [Odin — Staff Engineer](odin/agent.md)
* **Core Focus**: `WHAT`, `WHY`, `ARCHITECTURE`, `TRADEOFFS`, `LONG-TERM DESIGN`
* **Responsibilities**:
  - Reads and analyzes raw issue specifications from `docs/issues/*.md`.
  - Audits system-wide impacts across Spring Modulith boundaries, MongoDB schemas, and Temporal workflows.
  - Authors comprehensive [Feature Design Documents](odin/feature-design-template.md).
  - Evaluates distributed system tradeoffs (CAP, scalability, resilience, failure domains).
  - Performs final architectural gatekeeping before feature approval.

### 2. [Loki — SDE3 / Senior Software Engineer](loki/agent.md)
* **Core Focus**: `LOW-LEVEL DESIGN`, `CODE QUALITY`, `CORRECTNESS`, `DETAILED CODE REVIEW`
* **Responsibilities**:
  - Translates Odin's high-level architecture into actionable [Low-Level Design (LLD)](loki/low-level-design-template.md) guidance.
  - Establishes package structures, class responsibilities, DTO mappings, and transaction boundaries.
  - Conducts exhaustive code reviews using [Review Severity Guidelines](shared/review-severity.md).
  - Reviews Bruno API E2E collections for complete endpoint, security, and edge-case coverage.
  - Verifies that all acceptance criteria from the issue are met with high code quality.

### 3. [Thor — Software Developer](thor/agent.md)
* **Core Focus**: `IMPLEMENTATION`, `UNIT TESTS`, `INTEGRATION TESTS`, `APPLICATION E2E TESTS`, `BRUNO API E2E TESTS`, `BUILD VALIDATION`
* **Responsibilities**:
  - Implements clean, maintainable Java 25 / Spring Boot 4.1.0 code adhering to repository conventions.
  - Develops automated tests across all four testing layers (Unit, Integration with Testcontainers, App E2E, Bruno E2E).
  - Enforces JaCoCo line coverage quality gates ($\ge 95\%$) without artificial exclusions.
  - Executes Gradle build tasks (`./gradlew spotlessApply`, `./gradlew test`, `./gradlew check`).
  - Authors the [Issue Coverage Summary](workflow.md#4-issue-coverage-summary-schema) mapping requirements to tests.

---

## 🎯 Feature Source of Truth

All engineering initiatives (new features, bug fixes, enhancements, refactoring) must originate from Markdown issue files in:

```text
docs/issues/
```

Example backlog entries:
- `docs/issues/01-flow-02-realtime-websocket-market-data.md`
- `docs/issues/02-flow-03-temporal-workflow-market-lifecycle.md`
- `docs/issues/08-improvement-identity-refresh-token-mfa.md`

No agent may invent business behavior without explicit alignment with the issue backlog. If an issue is ambiguous or incomplete, assumptions and open questions must be documented explicitly in the design phase.

---

## 🔄 End-to-End Engineering Workflow

```text
Issue (docs/issues/*.md)
  ↓
Odin — Feature Design & Architecture
  ↓
Loki — Low-Level Design Guidance & Pre-Implementation Review
  ↓
Thor — Incremental Implementation & Multi-Layer Testing
  ↓
Loki — Detailed Code, Test & Bruno API Review
  ↓
Thor — Address Review Findings (Blocker / Critical / Major)
  ↓
Odin — Final Architecture & System Integrity Review
  ↓
Feature Approved & Merged
```

For complete details on state transitions, communication handoffs, and feedback loops, see [workflow.md](workflow.md).

---

## 🧪 Testing Responsibilities & Quality Gates

The system mandates a four-layer testing pyramid:
1. **Unit Tests**: Fast, deterministic tests validating domain entities, service logic, boundary conditions, and edge cases with Mockito and JUnit 5.
2. **Integration Tests**: Spring context slices, Spring Modulith boundary verifications, and real MongoDB persistence using Testcontainers.
3. **Application E2E Tests**: Full request-to-persistence flow validation for critical user journeys and workflow executions.
4. **Bruno API E2E Tests**: First-class API regression tests located in `bruno/`, covering happy paths, input validation, authentication/authorization, and idempotency.

### Quality Gate Mandates
- **JaCoCo Minimum Line Coverage**: $\ge 95\%$ (governed authoritatively by `build.gradle`).
- **Formatting**: Palantir Java Format, import ordering, trailing whitespace cleanup via `./gradlew spotlessApply`.
- **Full Verification**: `./gradlew check` must pass cleanly before final approval.

---

## 📂 Directory Organization

```text
.agents/
├── README.md                              # Multi-agent system overview & team structure
├── workflow.md                            # Comprehensive workflow, state transitions & handoff protocols
│
├── shared/                                # Shared repository-wide engineering standards
│   ├── project-context.md                 # Technical stack, architecture, modules & build configuration
│   ├── engineering-principles.md          # MAANG-inspired engineering philosophy & core tenets
│   ├── coding-standards.md                # Java 25 & Spring Boot 4.1.0 coding guidelines
│   ├── testing-standards.md               # 4-layer testing strategy, Testcontainers & Bruno API guidelines
│   ├── design-standards.md                # Spring Modulith boundaries, Temporal & Resilience4j design
│   ├── issue-management.md                # Issue intake standards, formatting & AC traceability
│   ├── review-severity.md                 # Review finding severities (BLOCKER, CRITICAL, MAJOR, MINOR, SUGGESTION)
│   └── definition-of-done.md              # Definitive completion checklist for all features
│
├── odin/                                  # Odin (Staff Engineer) assets
│   ├── agent.md                           # Persona, responsibilities, mission & success criteria
│   ├── skills.md                          # Staff-level system design, Modulith & resilience competencies
│   ├── feature-design-template.md         # 21-section High-Level Design document template
│   └── architecture-review-checklist.md   # Final architectural review checklist
│
├── loki/                                  # Loki (SDE3) assets
│   ├── agent.md                           # Persona, responsibilities, mission & success criteria
│   ├── skills.md                          # Senior-level LLD, code quality, and Bruno review competencies
│   ├── low-level-design-template.md       # 16-section Low-Level Design template
│   └── code-review-checklist.md           # Exhaustive code & test review checklist
│
└── thor/                                  # Thor (Software Developer) assets
    ├── agent.md                           # Persona, responsibilities, mission & success criteria
    ├── skills.md                          # Implementation, testing, Testcontainers & Bruno competencies
    ├── implementation-checklist.md        # Step-by-step developer execution checklist
    └── testing-checklist.md               # Multi-layer testing and quality gate verification checklist
```

---

## ✅ Definition of Done Summary

A feature or bugfix is **Done** only when:
1. Originates from a verified issue in `docs/issues/*.md`.
2. Odin's Feature Design and Loki's Low-Level Design are documented and aligned.
3. Production code is cleanly implemented with Spring Modulith module boundaries respected.
4. Unit, Integration, Application E2E, and Bruno API E2E tests are complete and passing.
5. Code coverage meets or exceeds the $95\%$ JaCoCo line coverage threshold.
6. Spotless formatting is applied (`./gradlew spotlessApply`) and `./gradlew check` passes.
7. Loki approves code quality, Bruno tests, and acceptance criteria fulfillment.
8. Odin grants final architectural sign-off with zero unresolved Blocker, Critical, or Major findings.

For the exhaustive checklist, see [shared/definition-of-done.md](shared/definition-of-done.md).
