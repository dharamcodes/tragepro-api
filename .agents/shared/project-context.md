# Project Context & Technical Architecture

This document provides the definitive technical context, architectural layout, dependency matrix, and build rules for `tragepro-api`. All agents must strictly align design, implementation, and testing decisions with these parameters.

---

## 🏛 1. Repository Identity & Base Package

* **Repository / Service Name**: `tragepro-api`
* **Domain**: Algorithmic Trading Platform & Trade Execution Engine
* **Base Package**: `com.tragepro.api`
* **Architecture Style**: Modular Monolith governed by **Spring Modulith**

---

## 💻 2. Technology Stack & Versions

The build configuration in `build.gradle` is authoritative. Agents must not modify `build.gradle` or introduce dependencies without explicit user authorization.

| Layer / Concern | Technology | Version / Configuration | Key Artifacts |
| :--- | :--- | :--- | :--- |
| **Language** | Java | **Java 25** (JVM Toolchain) | Modern language features (records, pattern matching, sealed types) |
| **Framework** | Spring Boot | **4.1.1** / `io.spring.dependency-management:1.1.7` | WebMVC, Security, Batch, Validation, Actuator |
| **Modular Architecture** | Spring Modulith | **2.1.1** | `spring-modulith-starter-core`, `spring-modulith-starter-mongodb`, `spring-modulith-actuator`, `spring-modulith-observability` |
| **Persistence** | MongoDB | Spring Boot Starter Data MongoDB | Reactive/blocking Mongo repositories, Testcontainers MongoDB |
| **Workflow Engine** | Temporal SDK | **1.38.0** | `temporal-sdk`, `temporal-spring-boot-starter` |
| **Resilience & Fault Tolerance** | Resilience4j | **2.4.0** | `resilience4j-spring-boot4` (CircuitBreaker, Retry, RateLimiter, Bulkhead) |
| **DTO Mapping** | MapStruct | **1.6.3** | `mapstruct`, `mapstruct-processor`, `lombok-mapstruct-binding:0.2.0` |
| **Boilerplate Reduction** | Project Lombok | Configured with `lombok.config` | `@RequiredArgsConstructor`, `@Getter`, `@Slf4j`, `@Builder` |
| **Authentication & Security** | JWT / Spring Security | `jjwt-api:0.13.0` | Stateless JWT token validation, Spring Security Filter Chain |
| **API Documentation** | Springdoc OpenAPI | **3.1.0** / Swagger UI **5.32.6** | `springdoc-openapi-starter-webmvc-ui` |
| **Real-Time Messaging** | WebSocket / STOMP | Spring Boot Starter WebSocket | Sub-second tick streaming and notification dispatch |
| **Batch Processing** | Spring Batch | Spring Boot Starter Batch | Bulk historical candle ingestion, reconciliation jobs |
| **AI Integration** | Spring AI | **1.0.0-RC1** | LLM-assisted strategy analysis and insight generation |
| **Unit & Integration Testing** | JUnit 5 & Testcontainers | JUnit Jupiter, Testcontainers MongoDB | `testcontainers-junit-jupiter`, `testcontainers-mongodb`, `spring-modulith-starter-test` |
| **Code Formatting** | Spotless | **8.9.0** | Palantir Java Format, import ordering, trim whitespace |
| **Code Coverage** | JaCoCo | Configured in Gradle | Minimum line coverage: **95%** |

---

## 📦 3. Spring Modulith Module Architecture

The codebase is organized into modular packages located under `src/main/java/com/tragepro/api/`.

```text
com.tragepro.api
├── domain/                 # OPEN Module (@ApplicationModule(type = OPEN)): Entities, Request/Response DTOs, Enums
├── common/                 # OPEN Module (@ApplicationModule(type = OPEN)): Kernel, Global Exceptions, Utilities, Common Mappers
├── identity/               # Encapsulated: User Authentication, Account Management & Profile Details
├── datafeed/               # Encapsulated: Market Data Ingestion, Historical Candles & Watchlists
├── strategy/               # Encapsulated: Strategy Evaluation, Rule Engine & Temporal Workflows
├── trading/                # Encapsulated: Order Lifecycle Execution & Portfolio Tracking
├── journal/                # Encapsulated: Trade Journaling, Audit Logs & Performance Notes
└── alert/                  # Encapsulated: Event-Driven Alerts, Listeners & Multi-Channel Dispatchers
```

### Module Internal Layering Convention

Each encapsulated business module enforces a strict four-layer internal structure:

```text
com.tragepro.api.<module>/
├── adapter/                # Public entry points marked with @NamedInterface("adapter")
│   └── <Module>Adapter.java
├── web/                    # REST Controllers (@RestController) exposing HTTP endpoints
│   └── <Feature>Controller.java
├── service/                # Service interfaces, implementations (service.impl), and MapStruct mappers (service.mapper)
│   ├── <Feature>Service.java
│   ├── impl/
│   │   └── <Feature>ServiceImpl.java
│   └── mapper/
│       └── <Feature>Mapper.java
└── core/                   # Internal business logic, repositories, contexts, events, and workflows
    ├── repository/
    ├── event/
    ├── workflow/
    └── model/
```

### Cross-Module Boundary Rules

1. **Adapter-Only Access**: Non-open modules (`identity`, `datafeed`, `strategy`, `trading`, `journal`, `alert`) must only interact with other modules via their exposed `@NamedInterface("adapter")` classes (e.g., `WatchListAdapter`, `TradingAdapter`, `NotificationAdapter`).
2. **Internal Encapsulation**: Classes in `service.impl`, `core`, and `web` must remain package-private or internal to their owning module.
3. **No Circular Dependencies**: Module dependencies must form a Directed Acyclic Graph (DAG), validated automatically by `ApplicationModules.of(Application.class).verify()`.
4. **Open Modules**: `domain` and `common` are open kernel modules accessible by all feature modules.

---

## 🔄 4. The 6 Core System Flows

Agents must design features in alignment with the platform's 6 core architectural flows:

1. **Flow 1: Core Security & JWT Authentication**: Incoming HTTP requests authenticate via `JWTAuthFilter` and `AuthenticationService`, populating the Spring Security Context.
2. **Flow 2: Market Data Ingestion**: `DataFeedController` routes feed requests through `FeedAdapterFactory` to production or dummy feeds, caching ticks in `DatafeedContext` before MongoDB persistence.
3. **Flow 3: Temporal Workflow Orchestration**: `DataInitActivityImpl` and `ActivityRegistry` coordinate distributed trading workflows, querying symbol watchlists across modules.
4. **Flow 4: Strategy Execution Pipeline**: `StrategyController` triggers `StrategyServiceImpl`, chaining Builder, Evaluator, and Executor components.
5. **Flow 5: Portfolio Position & Order Execution**: Trading signals hit `OrderController`, passing to `OrderManagerImpl` and `TradingServiceImpl`, journaling results via `JournalAdapter`.
6. **Flow 6: Event-Driven Multi-Channel Alert Notification**: Domain events broadcast over Spring's ApplicationEvent bus, where `AlertEventListener` routes notifications to Email, Telegram, or Webhooks via `NotificationChannelFactory`.

---

## ⚙️ 5. Build Configuration & Quality Gates

The `build.gradle` defines strict build and execution rules:

### JVM Flags (Java 25)
Required for testing and running with modern JVM features and ByteBuddy:
```text
--sun-misc-unsafe-memory-access=allow
-XX:+EnableDynamicAgentLoading
-Dnet.bytebuddy.experimental=true
```

### JaCoCo Quality Gate (95% Line Coverage)
- **Minimum Line Coverage Threshold**: `0.95` ($95\%$)
- **Authoritative Exclusions**:
  ```groovy
  def jacocoExcludes = [
      'com/tragepro/api/**/domain/**',
      'com/tragepro/api/**/config/**',
      'com/tragepro/api/**/*Config*',
      'com/tragepro/api/**/constant/**',
      'com/tragepro/api/**/*MapperImpl*',
      'com/tragepro/api/common/**',
      'com/tragepro/api/Application*'
  ]
  ```
- **Rule**: Do not add artificial exclusions. All service, business, core, and controller logic must be thoroughly tested.

### Spotless Formatting Rules
- **Java**: Palantir Java Format, import ordering, unused import removal, trim trailing whitespace.
- **Misc (md, gradle, gitignore)**: 4-space indent, trim trailing whitespace, end with newline.
- **YAML & Properties**: 2-space indent, trim trailing whitespace, end with newline.

### Mandatory Gradle Commands
```bash
# 1. Format all code according to project rules
./gradlew spotlessApply

# 2. Run unit and integration tests
./gradlew test

# 3. Generate JaCoCo coverage report
./gradlew jacocoTestReport

# 4. Full quality check (tests + JaCoCo 95% verification + Spotless check)
./gradlew check
```

---

## 🧪 6. Bruno API E2E Collection

REST API tests are maintained under `bruno/`:
- **Environments**: `bruno/environments/local.bru`, `dev.bru`, `prod.bru`
- **Integration Folders**: `bruno/integration/01-Auth/`, `02-Account/`, `03-Candle/`, `03a-Watchlist/`, `03b-Datafeed/`, `04-Journal/`, `06-Trading/`, `07-Cleanup/`
- Every new or modified REST endpoint must have corresponding Bruno test `.bru` files.
