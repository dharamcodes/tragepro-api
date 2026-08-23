# TragePro API - Project Issue Backlog & Roadmap

This directory contains the formal GitHub Issue specifications for all pending core flows and modular feature improvements in **TragePro API**.

---

## 📌 1. Pending Core Flows (Architecture & Pipeline Completion)

| Issue ID | Title | Module | Labels | Priority |
| :--- | :--- | :--- | :--- | :--- |
| [**#01**](01-flow-02-realtime-websocket-market-data.md) | **[Flow 02: Market Data] Real-Time WebSocket Market Data Streaming & Tick Aggregation Engine** | `datafeed` | `core-flow`, `streaming`, `websocket` | 🔴 High |
| [**#02**](02-flow-03-temporal-workflow-market-lifecycle.md) | **[Flow 03: Workflow] End-to-End Temporal Workflow Lifecycle & Market Schedule Orchestration** | `strategy`, `common` | `core-flow`, `temporal`, `orchestration` | 🔴 High |
| [**#03**](03-flow-04-strategy-backtesting-simulation-engine.md) | **[Flow 04: Strategy] Strategy Backtesting Simulation Engine & Performance Analytics** | `strategy` | `core-flow`, `backtesting`, `analytics` | 🔴 High |
| [**#04**](04-flow-04-technical-indicators-calculation-engine.md) | **[Flow 04: Strategy] Technical Indicator Calculation Library & Real-time Indicator Engine** | `strategy` | `core-flow`, `indicators`, `math` | 🔴 High |
| [**#05**](05-flow-05-broker-api-execution-gateway.md) | **[Flow 05: Trading] Broker API Execution Gateway & Smart Order Routing (SOR)** | `trading` | `core-flow`, `broker`, `execution` | 🔴 High |
| [**#06**](06-flow-05-pre-trade-risk-management-engine.md) | **[Flow 05: Trading] Pre-Trade Risk Management, Position Sizing & Trailing Stop-Loss Engine** | `trading` | `core-flow`, `risk-management`, `orders` | 🔴 High |
| [**#07**](07-flow-06-live-telegram-discord-alert-channels.md) | **[Flow 06: Alert] Live Telegram Bot & Discord Webhook Notification Channels** | `alert` | `core-flow`, `telegram`, `discord` | 🟡 Medium |

---

## 🚀 2. Feature Improvements (Optimizations, Security & Quality)

| Issue ID | Title | Module | Labels | Priority |
| :--- | :--- | :--- | :--- | :--- |
| [**#08**](08-improvement-identity-refresh-token-mfa.md) | **[Improvement: Identity] Refresh Token Rotation, Redis Token Blacklist & MFA / 2FA Support** | `identity` | `security`, `improvement`, `jwt` | 🔴 High |
| [**#09**](09-improvement-identity-rate-limiting-audit-logging.md) | **[Improvement: Identity] Rate Limiting & Security Audit Event Logging** | `identity` | `security`, `rate-limiting`, `audit` | 🟡 Medium |
| [**#10**](10-improvement-datafeed-multi-timeframe-resampling-caching.md) | **[Improvement: Datafeed] Dynamic Multi-Timeframe Candle Resampling & Redis Hot Cache Layer** | `datafeed` | `performance`, `caching`, `candles` | 🔴 High |
| [**#11**](11-improvement-strategy-rule-engine-dsl.md) | **[Improvement: Strategy] Dynamic Strategy Rule Engine DSL & Visual Rule Schema** | `strategy` | `dsl`, `rule-engine`, `improvement` | 🟡 Medium |
| [**#12**](12-improvement-trading-mark-to-market-pnl-sse.md) | **[Improvement: Trading] Real-Time Mark-to-Market (MTM) Portfolio Valuation & Server-Sent Events (SSE) Stream** | `trading` | `real-time`, `pnl`, `sse` | 🔴 High |
| [**#13**](13-improvement-journal-trade-analytics-equity-curve.md) | **[Improvement: Journal] Automated Trade Performance Analytics & Interactive Equity Curve Generator** | `journal` | `analytics`, `reporting`, `improvement` | 🟡 Medium |
| [**#14**](14-improvement-alert-throttling-deduplication.md) | **[Improvement: Alert] Notification Throttling, Deduplication & Delivery Retry Engine** | `alert` | `notifications`, `throttling`, `resilience` | 🟡 Medium |
| [**#15**](15-improvement-observability-opentelemetry-grafana.md) | **[Improvement: Observability] OpenTelemetry Distributed Tracing, Micrometer Metrics & Prometheus/Grafana Dashboards** | `common` | `metrics`, `opentelemetry`, `grafana` | 🟡 Medium |

---

## 🛠 GitHub Issue Templates

Issue templates are maintained in [`.github/ISSUE_TEMPLATE/`](../../.github/ISSUE_TEMPLATE/):
- [`pending_flow.yml`](../../.github/ISSUE_TEMPLATE/pending_flow.yml)
- [`feature_improvement.yml`](../../.github/ISSUE_TEMPLATE/feature_improvement.yml)
