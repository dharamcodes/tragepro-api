# [Improvement: Alert] Notification Throttling, Deduplication & Delivery Retry Engine

**Type**: Feature Improvement
**Module**: `alert`
**Labels**: `area:alert`, `notifications`, `throttling`, `resilience`
**Priority**: Medium

---

## 1. Current State & Limitations
[`AlertEventListener`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/alert/core/event/AlertEventListener.java) dispatches notifications immediately on every received `AlertEvent`. During volatile market conditions or rapid signal changes, this can cause notification storms (hundreds of alerts/min), flooding user channels (Email/Telegram) and violating external API rate limits. Additionally, there is no automatic retry mechanism if delivery fails.

---

## 2. Proposed Solution & Technical Design

1. **Alert Deduplication & Cooldown Window**:
   - Sliding window deduplicator: Suppress identical alert keys (e.g., `ALERT:{strategy}:{symbol}:{signal}`) emitted within a configurable cooldown period (e.g. 5 minutes).
2. **Notification Throttling & Batching**:
   - High-frequency minor alerts are batched into a single summary digest every 60 seconds.
   - Critical/Emergency alerts (Kill switch, SL hit) bypass throttling for immediate delivery.
3. **Resilient Delivery with Exponential Backoff & DLQ**:
   - Use Resilience4j retry with exponential backoff (1s, 2s, 4s, 8s).
   - If maximum retries fail, persist failed alert payload into a `failed_notifications` Dead Letter Queue (DLQ) in MongoDB for inspection and manual replay.

---

## 3. Implementation Checklist

- [ ] Implement `AlertDeduplicator` using Redis / in-memory Guava cache.
- [ ] Implement `NotificationDeliveryManager` with retry and DLQ fallback.
- [ ] Unit & integration tests for throttling, deduplication, and retry logic (>= 95% line coverage).
