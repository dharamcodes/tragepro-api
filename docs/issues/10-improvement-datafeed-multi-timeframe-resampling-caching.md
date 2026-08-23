# [Improvement: Datafeed] Dynamic Multi-Timeframe Candle Resampling & Redis Hot Cache Layer

**Type**: Feature Improvement
**Module**: `datafeed`
**Labels**: `area:datafeed`, `performance`, `caching`, `candles`
**Priority**: High

---

## 1. Current State & Limitations
Currently, candles are queried directly from MongoDB via `CandleRepository`. Fetching large datasets across multiple timeframes (1m, 5m, 15m, 1h, 1D) causes high I/O overhead on MongoDB. Additionally, non-base timeframes (e.g. 5m, 15m) are not automatically synthesized on-the-fly from 1m base candles.

---

## 2. Proposed Solution & Technical Design

1. **In-Memory Dynamic Resampling Engine**:
   - `CandleResampler`: Pure algorithmic converter that aggregates a stream of 1-minute `CandleResponse` objects into any arbitrary timeframe (3m, 5m, 15m, 30m, 1h, 4h, 1D).
   - Combines: Open (first open), High (max high), Low (min low), Close (last close), Volume (sum volume).
2. **Two-Tier Redis Caching Layer**:
   - **L1 Cache (Caffeine)**: In-JVM hot cache for latest intraday candles.
   - **L2 Cache (Redis)**: Distributed cache with TTL for historical daily/intraday queries keyed by `candles:{symbol}:{timeframe}:{date}`.
   - Invalidation triggered on new candle ingestion via [`CandleIngestAdapter`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/datafeed/core/feed/CandleIngestionProcessor.java).

---

## 3. Implementation Checklist

- [ ] Create `CandleResampler` in `datafeed.core.util` supporting arbitrary time bucket sizes.
- [ ] Add Redis cache configuration with Spring `@Cacheable` on [`CandleServiceImpl`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/datafeed/service/impl/CandleServiceImpl.java).
- [ ] Implement cache evict/update on candle save.
- [ ] Benchmarks and unit tests (>= 95% line coverage).
