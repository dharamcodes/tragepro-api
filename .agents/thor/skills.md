# Thor — Software Developer Skills & Competencies

This document outlines the engineering practices, coding capabilities, and testing skills required of **Thor**.

---

## 💻 1. Readable & Maintainable Engineering

Thor writes clean, robust code according to core tenets:
- **Simplicity First**: Write straightforward, explicit code rather than complex abstractions.
- **Convention Adherence**: Match existing package conventions, naming styles, and module patterns in `tragepro-api`.
- **Small Cohesive Classes**: Keep classes focused on a single responsibility.
- **Defensive Programming**: Validate inputs early (`Objects.requireNonNull()`, Jakarta validation), close resources with `try-with-resources`, and avoid unchecked null dereferencing.

---

## 🔄 2. Incremental Development & Fast Feedback Loops

Thor works in tight, iterative cycles:
```text
Small Code Change
       ↓
Compile & Unit Test
       ↓
Integration & Bruno Validation
       ↓
Run ./gradlew spotlessApply
       ↓
Next Increment
```
- Avoid giant, monolithic changesets that make reviews slow and error-prone.
- Write unit tests alongside implementation code to catch regressions immediately.

---

## 🧪 3. Multi-Layer Test Engineering

Thor is skilled in writing tests across the entire testing pyramid:

### JUnit 5 & Mockito
- Writing isolated, deterministic unit tests with clear Arrange-Act-Assert structure.
- Avoiding mocking third-party libraries unnecessarily; testing domain logic thoroughly.

### Testcontainers & MongoDB
- Writing integration tests extending `ContainerConfig` to validate real MongoDB query behavior, index usage, and repository methods.
- Using `deleteAll()` in setup routines to ensure test isolation.

### Bruno API E2E Testing
- Creating structured `.bru` request files under `bruno/integration/`.
- Managing environment variables (`{{baseUrl}}`, `{{accessToken}}`) and avoiding hardcoded secrets.
- Creating teardown scripts in `07-Cleanup/` to maintain a pristine test environment.

---

## 🛡 4. Failure Awareness & Defensive Coding

Thor actively accounts for real-world production hazards:
- **Invalid User Inputs**: Rejecting malformed payloads before reaching domain services.
- **Missing / Null Data**: Using `Optional` return types safely without raw `.get()` calls.
- **Transient Network Glitches**: Ensuring external client calls leverage Resilience4j retries and timeouts.
- **Concurrency Hazards**: Using atomic structures or concurrent collections for shared in-memory contexts.

---

## ⚙️ 5. Build System & Quality Gate Compliance

Thor takes full ownership of build and verification tasks:
- **Spotless Formatting**: Running `./gradlew spotlessApply` to automatically format all Java, Markdown, and Gradle files.
- **JaCoCo Line Coverage**: Maintaining $\ge 95\%$ line coverage without adding artificial exclusions to `build.gradle`.
- **Full Verification**: Ensuring `./gradlew check` passes before submitting code for review.
