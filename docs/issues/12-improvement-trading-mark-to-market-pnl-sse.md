# [Improvement: Trading] Real-Time Mark-to-Market (MTM) Portfolio Valuation & Server-Sent Events (SSE) Stream

**Type**: Feature Improvement
**Module**: `trading`
**Labels**: `area:trading`, `real-time`, `pnl`, `sse`
**Priority**: High

---

## 1. Current State & Limitations
Currently, [`TradingServiceImpl`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/trading/service/impl/TradingServiceImpl.java) stores static positions in MongoDB. Position PnL is calculated only on explicit demand or position close. Traders require live Mark-to-Market (MTM) calculations updated with every incoming market tick, plus a reactive Server-Sent Events (SSE) stream to push live PnL and order updates to client dashboards.

---

## 2. Proposed Solution & Technical Design

1. **In-Memory MTM Valuation Engine**:
   - Subscribes to `MarketTickEvent` from the `datafeed` module.
   - For every active open position in `TradingContext`:
     `UnrealizedPnL = (CurrentLTP - EntryPrice) * Quantity * SideMultiplier`.
   - Aggregates Total Portfolio MTM PnL, Realized PnL, and Total Return %.
2. **Server-Sent Events (SSE) Endpoint** (`/api/v1/trading/stream/positions`):
   - Exposes Spring `SseEmitter` stream.
   - Pushes serialized `PortfolioValuationResponse` every second or on significant price delta (> 0.1%).
3. **Auto-Liquidation Trigger**:
   - If total unrealized loss reaches account max-loss threshold, triggers emergency position close.

---

## 3. Implementation Checklist

- [ ] Create `MarkToMarketCalculator` in `trading.core`.
- [ ] Implement SSE controller endpoint in [`OrderController`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/trading/web/OrderController.java) / `TradingController`.
- [ ] Connect `DataEventListener` to trigger MTM re-computation on price updates.
- [ ] Unit & integration tests (>= 95% line coverage).
