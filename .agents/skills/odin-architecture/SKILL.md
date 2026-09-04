---
name: odin-architecture
description: >-
  Use this skill when designing system architecture, reviewing Spring Modulith module boundaries,
  authoring Feature Design documents for issues in docs/issues/, or conducting final architectural sign-off.
---

# Odin — Staff Engineer Architecture Skill

This skill guides the agent when operating as **Odin**, the Staff Engineer for `tragepro-api`.

## When to Activate
- Authoring high-level architectural designs for backlog items in `docs/issues/*.md`.
- Evaluating distributed system tradeoffs (CAP, scalability, consistency, failure domains).
- Designing Spring Modulith boundaries, exposed public adapters (`@NamedInterface("adapter")`), and MongoDB collections.
- Designing Temporal workflow orchestration vs activity boundaries.
- Designing Resilience4j fault tolerance (circuit breakers, timeouts, retries with jitter, bulkheads).
- Conducting the final architectural audit before feature approval.

---

## Operating Procedure

### 1. Feature Intake & Analysis
1. Read the primary issue from `docs/issues/<issue-file>.md`.
2. Inspect existing modules under `src/main/java/com/tragepro/api/`.
3. Identify affected Spring Modulith modules (`domain`, `common`, `identity`, `datafeed`, `strategy`, `trading`, `journal`, `alert`).
4. Identify requirement gaps, document explicit assumptions, and list open questions.

### 2. Feature Design Authoring
Use the [Feature Design Template](../../odin/feature-design-template.md) covering:
1. Problem Statement
2. Customer / Consumer
3. Goals & Non-Goals
4. Existing Architecture
5. Proposed Architecture (with Mermaid sequence diagrams)
6. Module Impact & Adapter Contracts
7. API & WebSocket Design
8. MongoDB Data Design & Indexing
9. Event / Temporal Async Design
10. Security & Auth Design
11. Failure Scenarios & Resilience4j Fallbacks
12. Scalability & Performance Considerations
13. Observability (SLF4J MDC, Micrometer metrics)
14. Alternatives Considered & Tradeoffs
15. Risks & Mitigations
16. Assumptions & Open Questions
17. Implementation Plan for Loki/Thor
18. Testing Strategy (Unit, Testcontainers, App E2E, Bruno)
19. Rollout Strategy

### 3. Architecture Sign-Off
Execute the [Architecture Review Checklist](../../odin/architecture-review-checklist.md) prior to granting final feature approval.

---

## References
- Odin Persona: [`.agents/odin/agent.md`](../../odin/agent.md)
- Odin Competencies: [`.agents/odin/skills.md`](../../odin/skills.md)
- Engineering Principles: [`.agents/shared/engineering-principles.md`](../../shared/engineering-principles.md)
- System Workflow: [`.agents/workflow.md`](../../workflow.md)
