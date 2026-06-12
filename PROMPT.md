# 🚀 Spring Boot & Modern Java (Java 21+) Project Workflow

This document serves as the standard development workflow and coding guideline for this Spring Boot project. The project is built using **Java 21**, and all code must leverage modern language features and high design standards.

---

## 🛠️ 1. Coding Standards & Modern Java (Java 21+) Guidelines

Always leverage modern Java features to write concise, readable, and highly maintainable code:

### 🔹 Core Language Features
- **Records**: Use `record` for immutable data carriers, such as API request payloads, response DTOs, and internal query models. Do not use verbose Lombok `@Data` classes for read-only data.
- **Pattern Matching for Switch**: Simplify complex conditional logic using type patterns in `switch` expressions.
- **Pattern Matching for `instanceof`**: Avoid explicit casting after type checks; use pattern matching directly (e.g., `if (obj instanceof String s) { ... }`).
- **Text Blocks**: Use text blocks (`"""`) for multi-line strings, SQL/Mongo queries, JSON payloads, or test mock fixtures.
- **Sealed Types**: Model closed domain hierarchies (like domain events, payment states, or specific error classifications) using `sealed` classes or interfaces.
- **Sequenced Collections**: Access elements in ordered collections using `SequencedCollection`, `SequencedSet`, or `SequencedMap` API methods (e.g., `.getFirst()`, `.getLast()`, `.reversed()`).
- **Stream API**: Use modern Stream features (e.g., `.toList()` instead of `.collect(Collectors.toList())`) and avoid side-effects inside streams.

### 🔹 Spring & Database Standards
- **Spring Data MongoDB**: Always use repository abstractions, custom repository implementations for complex queries, and proper transaction boundaries (`@Transactional`).
- **Spring RestClient**: Prefer Spring Boot's modern, fluent `RestClient` or `WebClient` over the legacy `RestTemplate` for external HTTP communication.
- **Dependency Injection**: Always use constructor-based dependency injection. Avoid `@Autowired` on fields.
- **Validation**: Enforce inputs validation using `jakarta.validation` annotations (e.g., `@NotNull`, `@Size`) on DTO/Record properties.

---

## 📋 2. Development & Testing Workflow

### 🚀 Step 1. [Develop Feature](ca://s?q=Develop_feature_in_Spring_Boot_using_Java_21)
- Implement functional logic adhering to the **Modern Java 21+ Guidelines** above.
- Organize code into clean packages/modules (e.g. following Spring Modulith boundaries).
- Avoid any hardcoded configuration, URLs, or magic numbers; externalize them into `application.yml` and bind them using type-safe `@ConfigurationProperties`.

### 🧪 Step 2. [Create Test Cases](ca://s?q=Create_JUnit_5_test_cases_using_Mockito)
- Write **JUnit 5** unit tests using Mockito for mocking dependencies.
- Add Spring Boot integration tests verifying database and WebSocket behavior using test containers or mocked contexts.
- Use Java 21 features (e.g., Records, Text Blocks) in test fixtures to keep tests clean and readable.

### 📡 Step 3. [Create Bruno Test Cases](ca://s?q=Create_Bruno_API_collection_tests)
- Define and document API request scenarios inside the **Bruno** collection (`bruno` directory).
- Verify status codes, JSON response payloads, and HTTP headers for both successful and error scenarios.

### 🔄 Step 4. [Run & Verify](ca://s?q=Run_Gradle_clean_check_spotless)
- Run code formatting check and compile checks:
  ```bash
  ./gradlew spotlessApply
  ./gradlew clean check --warning-mode all
  ```
- Ensure zero compiler warnings, zero spotless violations, and 100% test pass rates before proposing changes.

---

## ✅ Checklist
- [ ] Feature implemented using Java 21+ features (Records, Switch pattern matching, etc.)
- [ ] Config properties externalized to YAML (no hardcoded variables/URLs)
- [ ] Spotless formatting applied and verified locally
- [ ] JUnit 5 unit and integration tests written and passing
- [ ] Bruno collections created or updated for manual endpoint testing
- [ ] `./gradlew clean check` runs successfully with zero warnings
