# [Improvement: Observability] OpenTelemetry Distributed Tracing, Micrometer Metrics & Prometheus/Grafana Dashboards

**Type**: Feature Improvement
**Module**: `common`, cross-module
**Labels**: `area:observability`, `metrics`, `opentelemetry`, `grafana`
**Priority**: Medium

---

## 1. Current State & Limitations
While individual modules log structured messages with SLF4J, there is no unified distributed tracing across async events, Temporal workflows, virtual thread executors, and external broker calls. Production operations require latency metrics, error rates, JVM thread statistics, and trade execution timers exported to Prometheus/Grafana.

---

## 2. Proposed Solution & Technical Design

1. **Distributed Tracing (OpenTelemetry + Micrometer Tracing)**:
   - Propagate `traceId` and `spanId` across Spring Modulith event boundaries, REST HTTP requests, and virtual thread tasks.
   - Export trace spans to Jaeger / Zipkin / OpenTelemetry Collector.
2. **Key Custom Business Metrics (Micrometer)**:
   - `tragepro.orders.submitted.count` (tagged by symbol, broker, status).
   - `tragepro.orders.execution.latency` (Timer from signal emission to broker confirmation).
   - `tragepro.marketdata.ticks.ingested` (Counter).
   - `tragepro.strategy.evaluation.duration` (Summary distribution).
3. **Prometheus Endpoint & Pre-configured Grafana Dashboards**:
   - Enable Spring Boot Actuator Prometheus scraping at `/actuator/prometheus`.
   - Provide JSON dashboard templates for Grafana in `docs/dashboards/`.

---

## 3. Implementation Checklist

- [ ] Add `micrometer-registry-prometheus` and `micrometer-tracing-bridge-otel` dependencies.
- [ ] Instrument key execution points with `@Timed` and custom Micrometer meters.
- [ ] Create Grafana dashboard JSON models for Trading Operations and System Health.
- [ ] Unit tests for meter registrations and trace context propagation.
