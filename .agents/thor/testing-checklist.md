# Thor — Testing & Quality Gate Checklist

This checklist must be fully verified by **Thor** before submitting any work for code review.

---

## 🔬 1. Unit Tests
- [ ] Business logic and calculation engines tested in isolation.
- [ ] Request validation and boundary conditions covered (nulls, empty strings, negative values).
- [ ] Error branches and custom exception handling tested.
- [ ] Mockito mocks configured cleanly; AAA structure followed.
- [ ] Tests execute rapidly in-memory without Spring context startup.

---

## 🐳 2. Integration Tests
- [ ] Repository queries and custom MongoDB operations tested against real MongoDB via Testcontainers (`ContainerConfig`).
- [ ] Spring Modulith module verification test (`ApplicationModules.verify()`) passes.
- [ ] Security filter chain and authentication/authorization behavior verified.
- [ ] Test isolation guaranteed (data cleaned up in `@BeforeEach` / `@AfterEach`).

---

## 🌐 3. Application E2E Tests
- [ ] Full request-to-persistence flow tested for critical user journeys.
- [ ] Temporal workflow orchestration and activity execution verified where applicable.

---

## ⚡ 4. Bruno API E2E Tests
- [ ] New relevant endpoints have corresponding `.bru` files in `bruno/integration/`.
- [ ] Modified endpoints have updated Bruno requests.
- [ ] Happy path (2xx) responses and critical response JSON attributes asserted.
- [ ] Input validation failures (400) asserted.
- [ ] Authentication (401) and authorization (403) scenarios tested.
- [ ] Business error states (404, 409, 422) tested.
- [ ] Environment variables (`{{baseUrl}}`, `{{accessToken}}`) used exclusively; zero hardcoded secrets.
- [ ] Test cleanup requests added to `bruno/integration/07-Cleanup/` to maintain clean environments.

---

## 📊 5. Code Coverage & Quality Gates
- [ ] `./gradlew spotlessApply` executed with zero formatting issues.
- [ ] `./gradlew test` passes with $100\%$ success rate.
- [ ] `./gradlew jacocoTestReport` completes successfully.
- [ ] JaCoCo line coverage meets or exceeds the mandatory **95%** threshold.
- [ ] No artificial JaCoCo exclusions added to `build.gradle`.
- [ ] `./gradlew check` passes completely.
