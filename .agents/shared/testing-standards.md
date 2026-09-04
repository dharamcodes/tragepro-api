# Testing Standards & Quality Assurance

This document defines the testing strategy, standards across the four testing layers, Testcontainers usage, Bruno API E2E testing requirements, and JaCoCo code coverage gates for `tragepro-api`.

---

## 🧪 1. The 4-Layer Testing Strategy

```text
Unit Tests
     ↓
Integration Tests
     ↓
Application E2E Tests
     ↓
Bruno API E2E Tests
```

Every feature must select the appropriate testing layers based on behavioral risk and system boundaries.

---

## 🔬 Layer 1: Unit Tests

### Focus
- Isolated business logic, domain rules, calculation engines, request validation, and mapping transformations.

### Guidelines
- **Framework**: JUnit 5 (`org.junit.jupiter.api.*`) and Mockito (`org.mockito.*`).
- **Structure**: Follow the **Arrange-Act-Assert (AAA)** or Given-When-Then pattern.
- **Speed & Isolation**: Unit tests must execute in memory without bootstrapping the Spring application context or starting Docker containers.
- **Coverage**: Thoroughly test boundary conditions, null inputs, exception branches, and invalid states.

```java
@ExtendWith(MockitoExtension.class)
class TradingServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private TradingServiceImpl tradingService;

    @Test
    void executeOrder_withValidRequest_shouldPersistAndReturnResponse() {
        // Arrange
        OrderRequest request = new OrderRequest("BTCUSDT", OrderSide.BUY, BigDecimal.ONE, BigDecimal.valueOf(50000));
        OrderEntity entity = new OrderEntity();
        OrderResponse expectedResponse = new OrderResponse("ORD-1", "BTCUSDT", OrderStatus.FILLED);

        when(orderMapper.toEntity(request)).thenReturn(entity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(entity);
        when(orderMapper.toResponse(entity)).thenReturn(expectedResponse);

        // Act
        OrderResponse actualResponse = tradingService.executeOrder(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("ORD-1", actualResponse.orderId());
        verify(orderRepository).save(entity);
    }
}
```

---

## 🐳 Layer 2: Integration Tests

### Focus
- Interactions between Spring components, database queries against MongoDB, Spring Security filters, and Spring Modulith module verification.

### Guidelines
- **Testcontainers**: Extend the shared container configuration (`ContainerConfig`) to run tests against a real MongoDB Testcontainer instance.
- **Spring Modulith Verification**: Every feature affecting module boundaries must run `ApplicationModules.of(Application.class).verify()`.
- **Deterministic Asynchrony**: Never use `Thread.sleep()`. Use `Awaitility` for asynchronous assertions (e.g., event listeners or WebSocket messages).

```java
@SpringBootTest
class OrderRepositoryIntegrationTest extends ContainerConfig {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
    }

    @Test
    void findBySymbolAndStatus_shouldReturnMatchingOrders() {
        // Arrange
        OrderEntity order = new OrderEntity("BTCUSDT", OrderSide.BUY, BigDecimal.ONE, OrderStatus.NEW);
        orderRepository.save(order);

        // Act
        List<OrderEntity> results = orderRepository.findBySymbolAndStatus("BTCUSDT", OrderStatus.NEW);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getSymbol()).isEqualTo("BTCUSDT");
    }
}
```

---

## 🌐 Layer 3: Application E2E Tests

### Focus
- Complete end-to-end execution journeys spanning multiple internal layers or modules (e.g., HTTP Request $\to$ Controller $\to$ Service $\to$ Temporal Workflow $\to$ MongoDB $\to$ Response).

### Guidelines
- Verify critical business paths (e.g., user registration $\to$ JWT generation $\to$ placing an order $\to$ journal logging).
- Test realistic error cascades and cross-module event publishing.

---

## ⚡ Layer 4: Bruno API E2E Tests

Bruno API collections are **first-class deliverables** for all HTTP and REST API endpoints.

### Location & Structure
Bruno collections are maintained in `bruno/`:
```text
bruno/
├── environments/
│   ├── local.bru                  # Local environment variables (baseUrl, tokens)
│   ├── dev.bru
│   └── prod.bru
├── integration/
│   ├── 01-Auth/                   # Authentication & Token retrieval
│   ├── 02-Account/
│   ├── 03-Candle/
│   ├── 03a-Watchlist/
│   ├── 03b-Datafeed/
│   ├── 04-Journal/
│   ├── 06-Trading/
│   └── 07-Cleanup/                # Teardown & cleanup requests
└── collection.bru
```

### Mandatory Bruno Test Coverage Checklist
For every new or modified REST API endpoint, Thor must provide Bruno `.bru` files testing:
1. **Happy Path**: Valid request payloads yielding expected 2xx responses and validating key JSON attributes.
2. **Input Validation Failures**: Missing fields, negative amounts, malformed formats returning `400 Bad Request`.
3. **Authentication**: Requests without Authorization headers or with malformed tokens returning `401 Unauthorized`.
4. **Authorization**: Requests from unauthorized roles or tenants returning `403 Forbidden`.
5. **Business Errors**: Resource not found (`404`), duplicate idempotency keys (`409 Conflict`), or invalid state transitions.
6. **Data Cleanup**: Clean up test fixtures created during the test run via `07-Cleanup/` to keep local environments clean.

### Bruno Environment & Secret Safety
- Always reference environment variables: `{{baseUrl}}`, `{{accessToken}}`, `{{testOrderId}}`.
- **NEVER hardcode secrets, passwords, or live production credentials** in `.bru` files.

---

## 📊 JaCoCo Code Coverage Mandate (95%)

The repository enforces a strict **95% minimum line coverage** quality gate via Gradle.

### Exclusions Policy
The authoritative exclusions are configured in `build.gradle`:
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

### Rules
- **No Artificial Exclusions**: Never modify `build.gradle` to exclude complex logic from coverage.
- **No Hollow Tests**: Tests must assert genuine business behavior and edge cases, not just execute code paths to bump metrics.

---

## 🛠 Required Build & Verification Commands

Before any code is submitted for review, Thor must execute and pass:

```bash
# 1. Format code according to Spotless rules
./gradlew spotlessApply

# 2. Run all unit and integration test suites
./gradlew test

# 3. Generate JaCoCo coverage report
./gradlew jacocoTestReport

# 4. Verify full quality gate (compilation, Spotless, and JaCoCo 95% threshold)
./gradlew check
```
