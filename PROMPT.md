# 🚀 SDE3 Spring Boot & Java 25+ Engineering Guidelines

This document defines the high-standard engineering, architectural, security, and operational guidelines for this project. Every feature implementation, code change, and system design must adhere to these standards.

---

## 🏛️ 1. Architecture & Design Principles (SDE3 Level)

### 🔹 Clean & Modular Design
- **Single Responsibility (SRP)**: Classes must have one reason to change. Deconstruct heavy service classes into single-purpose helpers, handlers, or domain-specific logic units.
- **Hexagonal / Clean Architecture**: Keep core domain logic independent of external adapters (e.g., MongoDB, WebSockets, REST APIs). Always define interfaces at boundary levels.
- **Spring Modulith Boundaries**: Enforce strict module encapsulation. Package-private visibility should be the default for internal component implementations; expose only public API interfaces/DTOs.

### 🔹 Immutability & Thread Safety
- **Immutable Types**: Use `record` for all read-only data carriers (DTOs, event payloads, configuration properties).
- **Thread-Safe Collections**: Avoid using `HashMap` or `ArrayList` in shared/singleton beans where concurrent reads/writes occur. Use `ConcurrentHashMap`, `CopyOnWriteArrayList`, or atomic wrappers (`AtomicInteger`, `AtomicReference`).
- **Stateless Singletons**: Spring beans (`@Service`, `@Component`) must be stateless. State must be passed through method arguments or kept within thread-safe scopes.

---

## 🔒 2. Defensive Coding & Security Standards

### 🔹 Input Validation & Sanitization
- **Strict Validation**: Always validate user inputs at the boundary layer using `jakarta.validation` (`@NotNull`, `@Size`, `@Pattern`, `@Email`).
- **NoSQL Injection Prevention**: Never construct query strings dynamically. Use Spring Data MongoDB's type-safe `Query`/`Criteria` builders or parameterized repository methods.
- **Fail-Fast**: Validate parameters immediately on entry to methods. Use `Objects.requireNonNull()` or custom precondition checks.

### 🔹 Handling Sensitive Data
- **Secret Management**: Never hardcode credentials, API keys, tokens, or private keys. Externalize all secrets using environment variables or secret vaults.
- **Log Masking & PII Protection**: Mask sensitive data (such as passwords, credit card numbers, PII, and JWT tokens) in logging statements. Never print entire DTOs containing sensitive variables.
- **Secure Serialization**: Exclude sensitive fields from JSON serialization using `@JsonIgnore` or by designing specific public-facing records that exclude the sensitive parameters.

---

## ⚡ 3. Resilience, Fault Tolerance & Performance

### 🔹 Network Resilience
- **Strict Timeouts**: Every HTTP client (`RestClient`, `WebClient`) and WebSocket connection must configure explicit connect, read, and write timeouts. Never use infinite defaults.
- **Fault-Tolerance Patterns**: Implement circuit breakers, rate limiters, and retry mechanisms with **exponential backoff and random jitter** for all external calls.
- **Graceful Degradation**: Always define fallback mechanisms or default states in case of external system failures.

### 🔹 Resource & Database Optimization
- **Try-With-Resources**: Always close resources (streams, connections, files) using `try-with-resources`.
- **MongoDB Indexing**: Ensure every query matches a defined index. Avoid collation/sorting without matching indexes, and use projection to load only necessary fields instead of full documents.
- **Lazy Evaluation**: Leverage the lazy nature of Java Streams. Avoid collecting to lists intermediately if further operations can be chained.

---

## 📊 4. Observability, Logging & Operational Readiness

### 🔹 Structured Logging & Context
- **Appropriate Logging Levels**: Use `TRACE` for high-volume debug details, `DEBUG` for operational diagnostics, `INFO` for critical flow transitions, `WARN` for recoverable errors, and `ERROR` for system-critical issues.
- **Contextual Logging (MDC)**: Include correlation IDs, user IDs, or transaction IDs in the `MDC` to trace asynchronous workflows across threads (especially inside task schedulers or WebSockets).
- **Clear Messages**: Logs must be actionable. Instead of logging `log.error("Failed")`, use structured logging: `log.error("Failed to process transaction for userId: {}. Reason: {}", userId, ex.getMessage(), ex)`.

### 🔹 Health & Metrics
- **Micrometer Metrics**: Expose critical operational metrics (connection pools, execution latency, error counts) using Micrometer's `MeterRegistry`.
- **Liveness & Readiness Probes**: Ensure Spring Boot Actuator is configured with custom health indicators representing database connectivity and key downstream API statuses.

---

## 🧪 5. Testing & Code Quality Assurance

### 🔹 Test Pyramid & Isolation
- **Mocking Strategy**: Keep unit tests isolated. Mock downstream interfaces using Mockito and focus assertions on business logic behavior, boundary conditions, and error-handling paths.
- **Integration Tests (Testcontainers)**: Use Testcontainers to run integration tests against a real MongoDB instance. Avoid embedding mock databases in integration profiles to prevent behavior mismatch.
- **Deterministic Tests**: Avoid `Thread.sleep()` in tests. Use `Awaitility` to wait for asynchronous events to complete deterministically.

---

## ✅ Checklist for SDE3 Pull Requests
- [ ] Code follows SOLID principles and packages are modular.
- [ ] No hardcoded secrets, configuration parameters, or URLs.
- [ ] Connect and read timeouts configured on all network-facing clients.
- [ ] Database queries are indexed and projected efficiently.
- [ ] Input data validated at boundaries; sanitization applied where needed.
- [ ] Logs are structured, appropriately leveled, and sensitive data is masked.
- [ ] Fault tolerance (retry, fallback) designed for unreliable external dependencies.
- [ ] Spotless check and `./gradlew clean check` pass with zero compile/Lombok warnings.
