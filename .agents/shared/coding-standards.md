# Coding Standards & Best Practices

This document defines the strict programming standards for Java 25, Spring Boot 4.1.1, and related frameworks within the `tragepro-api` codebase.

---

## ☕ 1. Java 25 Language Standards

### 🔹 Records for Data Carriers
Use standard Java `record` declarations for:
- Request and Response DTOs
- Spring Modulith event payloads
- Configuration property carriers
- Read-only data transfer objects

```java
// Preferred: Immutable record for DTOs and event payloads
public record OrderRequest(
    @NotBlank(message = "Symbol must not be blank") String symbol,
    @NotNull(message = "Side must be specified") OrderSide side,
    @Positive(message = "Quantity must be positive") BigDecimal quantity,
    @Positive(message = "Price must be positive") BigDecimal price
) {}
```

### 🔹 Pattern Matching & Switch Expressions
Leverage Java 25 pattern matching for `instanceof` and modern `switch` expressions:

```java
// Preferred: Pattern matching switch
public BigDecimal calculateFee(OrderType orderType, BigDecimal amount) {
    return switch (orderType) {
        case MARKET -> amount.multiply(BigDecimal.valueOf(0.001));
        case LIMIT -> amount.multiply(BigDecimal.valueOf(0.0005));
        case STOP_LOSS, STOP_LIMIT -> amount.multiply(BigDecimal.valueOf(0.0015));
    };
}
```

### 🔹 Immutability & Thread Safety
- **Stateless Singletons**: Spring beans (`@Service`, `@Component`, `@RestController`) must remain completely stateless.
- **Thread-Safe Collections**: For shared in-memory state (such as `DatafeedContext`), never use raw `HashMap` or `ArrayList`. Always use `ConcurrentHashMap`, `CopyOnWriteArrayList`, or atomic structures (`AtomicReference`, `AtomicLong`).
- **Immutable Collections**: Use `List.copyOf()`, `Set.copyOf()`, or `Collections.unmodifiableList()` when exposing collections outside a class.

---

## 🌱 2. Spring Boot 4.1.1 & Spring Modulith Standards

### 🔹 Dependency Injection
- **Mandatory Constructor Injection**: Use Lombok's `@RequiredArgsConstructor` on all Spring components.
- **Prohibited**: `@Autowired` on private fields (Field Injection).

```java
// Correct Pattern
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AlertEventPublisher alertPublisher;
}
```

### 🔹 Controller & API Design
- Annotate REST endpoints with `@RestController` and `@RequestMapping("/api/v1/...")`.
- Enforce input validation using `@Valid` or `@Validated` alongside `jakarta.validation` constraints (`@NotNull`, `@NotBlank`, `@Size`, `@Positive`, `@Pattern`).
- Never expose internal MongoDB `@Document` entities in controller responses; always map to dedicated response DTO records via MapStruct.

### 🔹 Centralized Exception Handling
- Exceptions must extend standard project base exceptions under `com.tragepro.api.common.exception`.
- Return structured error responses conforming to standard HTTP error models.
- **Prohibited**: Catching generic `Exception` or `Throwable` without rethrowing or structured recovery.
- **Prohibited**: Empty `catch` blocks.

```java
// Prohibited
try {
    processOrder();
} catch (Exception e) {
    // silently ignored - NEVER DO THIS
}

// Correct Pattern
try {
    processOrder();
} catch (BrokerConnectionException ex) {
    log.error("Failed to connect to broker for order {}: {}", orderId, ex.getMessage(), ex);
    throw new ServiceUnavailableException("Broker gateway temporarily unavailable", ex);
}
```

---

## 🗺 3. MapStruct & Object Mapping

- Use MapStruct interfaces located in `com.tragepro.api.<module>.service.mapper`.
- Configure MapStruct with `componentModel = "spring"` and `unmappedTargetPolicy = ReportingPolicy.IGNORE`.
- Use Lombok-MapStruct binding to ensure seamless compilation with Java 25.

```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity entity);
    OrderEntity toEntity(OrderRequest request);
}
```

---

## 🍃 4. MongoDB Persistence Standards

- Entities must be annotated with `@Document(collection = "...")`.
- Always define appropriate indexes (`@Indexed`, `@CompoundIndex`) matching production query patterns.
- Avoid dynamic string concatenation in MongoDB queries; use Spring Data MongoDB type-safe `Query`/`Criteria` or repository query methods.
- Use projection to fetch only required fields in high-frequency queries.

---

## 🛡 5. Defensive Coding & Best Practices

### Summary of Best Practices:
| Aspect | Preferred Practice | Anti-Pattern to Avoid |
| :--- | :--- | :--- |
| **Null Safety** | `Objects.requireNonNull()`, `Optional` return types for lookups | `return null;` from collection-returning methods, unchecked dereferences |
| **Resource Management** | `try-with-resources` for streams, connections, sockets | Manual `.close()` calls in `finally` blocks |
| **Constants** | Strongly typed `enum` or package-private `static final` constants | Magic strings or magic numbers scattered in methods |
| **String Formatting** | Structured SLF4J parameterized logging: `log.info("Order {} processed", id)` | String concatenation in logging: `log.info("Order " + id + " processed")` |
| **Security** | Mask sensitive fields in logs; exclude credentials from toString/JSON | Logging raw JWT tokens, API keys, or user credentials |
| **Method Cohesion** | Small, focused methods ($\le 30$ lines) with single responsibility | Giant monolith methods performing multiple unrelated tasks |
