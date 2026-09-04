# Engineering Principles & Philosophy

This document articulates the shared engineering philosophy governing the multi-agent system. These principles draw upon publicly recognized best practices from world-class engineering organizations (Google, Amazon, Meta, Apple, Netflix) adapted specifically to the `tragepro-api` architecture.

---

## 🏛 MAANG-Inspired Engineering Tenets

### 1. Google-Inspired: Maintainability & Simplicity
* **Long-Term Maintainability**: Optimize for long-term codebase health rather than fast, hacky deliveries.
* **Simplicity Over Cleverness**: Code must be immediately understandable to any engineer reading it for the first time.
  ```text
  Simple > Clever
  Readable > Compact
  Explicit > Implicit
  Maintainable > Over-engineered
  ```
* **Design Before Code**: Significant architectural changes must undergo design and review before touching production code.
* **Automated Safety Nets**: Rely on comprehensive test suites and Modulith verification to prevent regressions.

---

### 2. Amazon-Inspired: Customer Obsession & Ownership
* **Work Backwards From the Business Need**: Every feature design must address:
  ```text
  1. Who is the customer / API consumer?
  2. What exact trading or business problem are we solving?
  3. How do we verify the feature functions as expected?
  4. How do we monitor its health in production?
  5. What happens when downstream systems fail?
  ```
* **End-to-End Ownership**: Engineers (Odin, Loki, Thor) own their changes throughout the entire lifecycle—from design to production metrics, error logging, and Bruno API tests.

---

### 3. Meta-Inspired: Rapid Iteration With Uncompromising Safety
* **Fast Feedback Loops**: Break features into small, incremental, verifiable PRs/commits.
* **Zero Compromise on Quality Gates**: Speed must never come at the expense of:
  - Architectural integrity
  - Test coverage ($\ge 95\%$)
  - Security validation
  - Production resilience
* **Pragmatic Scaling**: Design for real platform constraints rather than premature theoretical hyper-scale.

---

### 4. Apple-Inspired: Craftsmanship & API Elegance
* **API Predictability**: REST and WebSocket contracts must be intuitive, consistent, and strictly validated.
* **Minimalist Surface Area**: Expose only what is strictly necessary. Keep internal classes, models, and helper methods internal to their owning Spring Modulith module.
* **Attention to Detail**: Naming conventions, error responses (RFC 7807 problem details), and domain terminology must be precise.

---

### 5. Netflix-Inspired: Resilience & Failure-Aware Design
* **Design for Failure**: Assume every external dependency (broker APIs, WebSocket feeds, MongoDB, Temporal workers) will experience latency, network partitions, or outages.
* **Distributed System Invariants**: For every async or distributed call, always answer:
  ```text
  • What happens when the remote service is slow? (Explicit timeouts)
  • What happens when the network fails? (Circuit breakers & retries with jitter)
  • What happens when requests are duplicated? (Idempotency keys & unique constraints)
  • What happens during partial system failure? (Graceful degradation & fallback states)
  • What happens when downstream capacity is exhausted? (Rate limiting & backpressure)
  ```

---

## 🛡 Shared Engineering Mandates

### 1. Inspect Before Changing
**Never assume repository structure or invent patterns.** Before writing or proposing any code:
1. Inspect existing modules under `com.tragepro.api.*`.
2. Inspect existing Spring Modulith adapters (`@NamedInterface("adapter")`).
3. Inspect existing test configurations (`ContainerConfig`, Mockito patterns).
4. Inspect existing Bruno API collections in `bruno/`.
5. Follow established repository conventions instead of introducing parallel paradigms.

---

### 2. Minimal Change Principle
* Make the smallest, most surgical change that correctly solves the stated problem.
* Avoid opportunistic, unrelated refactorings or cosmetic reorganizations that obscure the core change.
* Keep pull requests and changesets focused and easy to audit.

---

### 3. No Invented Requirements
* The primary source of truth for business requirements is `docs/issues/*.md`.
* Never invent unrequested business logic, endpoints, or entity attributes silently.
* When requirements are ambiguous or incomplete:
  1. Inspect the issue description and acceptance criteria.
  2. Inspect related documentation in `docs/` and `README.md`.
  3. Inspect existing code implementations.
  4. Explicitly document assumptions and open questions in the Feature Design or LLD.

---

### 4. Strict Backward Compatibility
Always assess the impact of changes on existing consumers:
* **REST APIs**: Do not remove fields, change data types, or modify status codes on existing public endpoints without deprecation paths.
* **MongoDB Schemas**: Ensure new entity fields are backward-compatible with existing persisted documents (e.g., default values, null safety).
* **Spring Application Events**: Do not alter event record signatures consumed by cross-module listeners without coordinated updates.
* **Temporal Workflows**: Maintain workflow determinism; never modify existing workflow execution code in ways that break running workflow histories.

---

### 5. Production Ownership, Observability & Security
* **Structured Logging**: Use SLF4J (`@Slf4j`) with appropriate log levels (`DEBUG`, `INFO`, `WARN`, `ERROR`). Always include contextual identifiers (e.g., `userId`, `orderId`, `symbol`).
* **Zero Secret Logging**: **NEVER** log sensitive data:
  - Passwords and raw hashes
  - JWT tokens and signing secrets
  - Broker API keys and secrets
  - Personal Identifiable Information (PII)
* **Metrics & Diagnostics**: Expose actionable Micrometer metrics and health indicators for critical trading operations.
