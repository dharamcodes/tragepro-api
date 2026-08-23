# [Improvement: Journal] Automated Trade Performance Analytics & Interactive Equity Curve Generator

**Type**: Feature Improvement
**Module**: `journal`
**Labels**: `area:journal`, `analytics`, `reporting`, `improvement`
**Priority**: Medium

---

## 1. Current State & Limitations
[`JournalServiceImpl`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/journal/service/impl/JournalServiceImpl.java) provides CRUD operations on trade journal logs with basic symbol filtering. Traders need comprehensive post-trade analytics to analyze performance: Win/Loss streaks, Average R:R (Risk-to-Reward) achieved, Best/Worst trades, Strategy-wise PnL breakdowns, and cumulative Equity Curve data series.

---

## 2. Proposed Solution & Technical Design

1. **Trade Analytics Engine**:
   - Computes statistical metrics across closed trade journals:
     - Win Rate % (`winningTrades / totalTrades`)
     - Profit Factor (`grossProfit / grossLoss`)
     - Average Win vs Average Loss amount
     - Largest Winning Trade vs Largest Losing Trade
     - Max Consecutive Wins vs Max Consecutive Losses
     - Average Trade Holding Time
2. **Cumulative Equity Curve Generator**:
   - Produces a chronologically ordered array of `{ date, cumulativePnL, drawdownPercent }` points suitable for chart rendering.
3. **Journal Analytics Endpoint**:
   - `GET /api/v1/journal/analytics?fromDate=...&toDate=...&strategy=...`
   - `GET /api/v1/journal/equity-curve`

---

## 3. Implementation Checklist

- [ ] Create `JournalAnalyticsCalculator` in `journal.service.impl`.
- [ ] Add analytics endpoints in [`JournalController`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/journal/web/JournalController.java).
- [ ] Add aggregation queries in `JournalRepository`.
- [ ] Tests and validation (>= 95% line coverage).
