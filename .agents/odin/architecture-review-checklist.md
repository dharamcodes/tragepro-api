# Odin — Final Architecture Review Checklist

This checklist is executed by **Odin** prior to final feature sign-off and approval.

---

## 🏛 1. Architectural Integrity & Modulith Boundaries
- [ ] **Modulith Encapsulation**: Module internals remain package-private or unexposed; only `@NamedInterface("adapter")` classes are accessed cross-module.
- [ ] **Acyclic Dependencies**: No circular dependencies between modules; verified via `ApplicationModules.of(Application.class).verify()`.
- [ ] **Layering Discipline**: Controllers contain zero business logic; domain entities are not leaked directly to REST responses.
- [ ] **Minimal Change Principle**: The implementation introduces only the changes necessary to solve the issue, avoiding unrelated structural churn.

---

## 🍃 2. Data Modeling & Persistence Architecture
- [ ] **MongoDB Indexing**: All new or updated queries are supported by explicit single-field or compound indexes.
- [ ] **Schema Scalability**: Document growth patterns are bounded; large unbounded arrays are avoided in favor of separate collections or time-series bucketing.
- [ ] **Data Integrity & Atomicity**: Financial or state-critical operations maintain consistency through atomic operations or multi-document transactions where appropriate.

---

## ⏱ 3. Temporal Workflow & Distributed Design
- [ ] **Workflow Determinism**: Workflow code contains zero non-deterministic operations (no direct I/O, no random generators, no system clocks).
- [ ] **Activity Boundaries**: Side-effect operations and remote calls are encapsulated in Temporal Activities.
- [ ] **Idempotency & Retries**: Activities configure appropriate retry policies, timeouts, and idempotency keys.

---

## ⚡ 4. Resilience & Failure Modes
- [ ] **External Client Resilience**: All external integrations (brokers, data feeds, external APIs) configure timeouts, circuit breakers, and rate limiters.
- [ ] **Graceful Degradation**: Clear fallback mechanisms are active when external dependencies become unavailable.
- [ ] **No Thundering Herds**: Retries incorporate exponential backoff with random jitter.

---

## 🔒 5. Security & Secret Safety
- [ ] **Zero Secret Logging**: Logs contain no passwords, API keys, JWT tokens, or sensitive credentials.
- [ ] **Authorization Boundaries**: Sensitive endpoints enforce appropriate security checks and role-based permissions.
- [ ] **Defensive Input Handling**: Injection risks are mitigated; inputs are validated at API entry points.

---

## 📊 6. Observability & Telemetry
- [ ] **Contextual Logging**: Structured logs include correlation IDs, user IDs, or order IDs via SLF4J / MDC.
- [ ] **Operational Metrics**: Critical business transitions and failure rates are exposed via Micrometer meters.

---

## 🎯 7. Acceptance Criteria Verification
- [ ] **Source Issue Fulfillment**: Every acceptance criterion from `docs/issues/<issue-file>.md` is addressed in the final implementation.
- [ ] **Test Hierarchy**: Unit, Integration, Application E2E, and Bruno API E2E tests provide robust validation.

---

## 🚫 What Odin Does NOT Focus On
To maintain high efficiency, Odin delegates the following concerns to automated tools and Loki:
- Spotless code formatting and import ordering (handled by `./gradlew spotlessApply`).
- Minor local variable naming and syntax styling (handled by Loki).
- Granular unit test mock details (handled by Loki).
