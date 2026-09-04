# Design Standards & System Architecture

This document sets the architectural design standards for `tragepro-api`, covering Spring Modulith boundaries, layer separation, Temporal workflow patterns, and Resilience4j fault tolerance.

---

## 🏛 1. Spring Modulith Architectural Boundaries

The platform is designed as a **Modular Monolith** using **Spring Modulith**. Encapsulation is enforced at compile time and verified during test execution via `ApplicationModules.verify()`.

```text
+-------------------------------------------------------------+
|             OPEN Modules: domain / common                   |
+-------------------------------------------------------------+
         ▲                 ▲                  ▲
         │                 │                  │
+-----------------+ +-----------------+ +-----------------+
| identity module | | datafeed module | | strategy module |
|  [@Named...("   | |  [@Named...("   | |  [@Named...("   |
|   adapter")]    | |   adapter")]    | |   adapter")]    |
+-----------------+ +-----------------+ +-----------------+
```

### Encapsulation Rules
1. **Open Kernel Modules**:
   - `domain`: Contains global entities, DTO records, enums, and data models. Annotated with `@ApplicationModule(type = OPEN)`.
   - `common`: Contains global utilities, exception handlers, security config, base models. Annotated with `@ApplicationModule(type = OPEN)`.
2. **Encapsulated Feature Modules**:
   - Feature modules (`identity`, `datafeed`, `strategy`, `trading`, `journal`, `alert`) must hide internal implementation classes (`service.impl.*`, `core.*`).
   - Cross-module communication **must solely route through public Adapter classes** annotated with `@NamedInterface("adapter")` in the module's `adapter` package (e.g., `WatchListAdapter`, `OrderAdapter`).
3. **No Circular Dependencies**:
   - Module dependencies must remain strictly unidirectional. If Module A depends on Module B, Module B must never directly or indirectly depend on Module A. Use Spring Application Events for decoupling when needed.

---

## 📐 2. Layer Separation & Dependency Direction

Each module enforces a clean, four-layer architecture:

```text
API Layer (web/)
   │
   ▼
Application Layer (service/ & service.impl/)
   │
   ▼
Domain & Core Logic (core/ & domain/)
   │
   ▼
Infrastructure & Persistence (core/repository/ & external clients)
```

### Responsibilities by Layer
- **`web/` (Controllers)**: Receives HTTP requests, performs input validation (`@Valid`), delegates to internal Services, and returns response DTOs. Controllers must contain **zero business logic**.
- **`service/` (Application Services)**: Coordinates application use cases, transaction boundaries (`@Transactional`), domain workflows, and entity-to-DTO mappings via MapStruct.
- **`adapter/` (Public Module Facades)**: Exposes `@NamedInterface("adapter")` entry points for other Spring Modulith modules.
- **`core/` (Internal Logic & Repositories)**: Contains domain calculation engines, MongoDB repositories, workflow activity implementations, and internal event listeners.

---

## ⏱ 3. Temporal Workflow & Activity Design

Temporal coordinates long-running, multi-step distributed trading workflows (e.g., market data initialization, strategy execution pipelines, multi-exchange order routing).

```text
Temporal Client / Trigger
          │
          ▼
   Workflow Definition (Deterministic Orchestration)
          │
          ├──> Activity 1: Fetch Watchlist (via WatchListAdapter)
          ├──> Activity 2: Ingest Historical Candles
          └──> Activity 3: Evaluate Strategy & Execute Signals
```

### Temporal Invariants
1. **Workflow Determinism**:
   - Workflow code must be 100% deterministic.
   - **Prohibited in Workflows**: `UUID.randomUUID()`, `System.currentTimeMillis()`, raw `Thread.sleep()`, direct I/O, database access, or network calls.
   - All external I/O and non-deterministic logic must be delegated to **Temporal Activities**.
2. **Activity Idempotency & Retries**:
   - Activities must be designed to be safely retried upon transient failure.
   - Configure explicit `ActivityOptions` with connect timeouts, start-to-close timeouts, and backoff retry policies.

---

## ⚡ 4. Resilience4j Fault Tolerance Patterns

For all external dependencies (brokers, market data feeds, remote HTTP services), Thor and Loki must implement Resilience4j protections:

```text
Incoming Call ──> RateLimiter ──> CircuitBreaker ──> Retry (w/ Jitter) ──> Bulkhead ──> External Service
                                                                              │
                                                                       Fallback State
```

1. **Circuit Breakers**: Prevent cascading failure when downstream dependencies are degraded.
2. **Timeouts**: Mandatory on all network clients (`RestClient`, `WebSocketClient`). Never allow unbounded connection waits.
3. **Retries with Exponential Backoff & Jitter**: Avoid thundering herds on remote services during recovery.
4. **Graceful Fallbacks**: Provide safe, degraded responses or queue messages when downstream capacity is exhausted.

---

## 🚫 5. Anti-Patterns to Avoid

- **Premature Abstraction**: Do not create complex generic frameworks or inheritance hierarchies until proven necessary by multiple distinct use cases.
- **Entity Leaks**: Never return `@Document` MongoDB entities directly to API clients.
- **God Classes**: Break classes that exceed 300 lines or handle multiple disjoint responsibilities into focused collaborators.
- **Hidden Side Effects**: Methods must perform what their signature describes without surprising state mutations.
