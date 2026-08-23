# [Improvement: Strategy] Dynamic Strategy Rule Engine DSL & Visual Rule Schema

**Type**: Feature Improvement
**Module**: `strategy`
**Labels**: `area:strategy`, `dsl`, `rule-engine`, `improvement`
**Priority**: Medium

---

## 1. Current State & Limitations
Strategies are currently configured via static YAML/JSON files and fixed Java pipeline definitions. Traders cannot dynamically define complex, nested logical conditions (e.g., `(RSI(14) < 30 AND Close > EMA(200)) OR (MACD_Hist crosses above 0)`) without writing Java code and recompiling the application.

---

## 2. Proposed Solution & Technical Design

1. **Strategy Rule DSL Engine (MVEL / Spring EL / Custom AST)**:
   - Provide a safe expression evaluation engine that evaluates Boolean expressions over `StrategyContext` and indicator outputs.
   - Example rule definition:
     ```json
     {
       "entryRule": {
         "operator": "AND",
         "conditions": [
           { "indicator": "RSI_14", "comparison": "LESS_THAN", "value": 30 },
           { "indicator": "CLOSE", "comparison": "GREATER_THAN", "value": "EMA_200" }
         ]
       },
       "exitRule": {
         "operator": "OR",
         "conditions": [
           { "indicator": "RSI_14", "comparison": "GREATER_THAN", "value": 70 },
           { "stopLossPercent": 1.5 }
         ]
       }
     }
     ```
2. **StrategyEvaluator Integration**:
   - Update `StrategyEvaluator` to parse and evaluate the AST / Expression against current market data state.
3. **Validation & Sandbox**:
   - Strict expression sandboxing to prevent arbitrary code execution / reflection vulnerabilities.

---

## 3. Implementation Checklist

- [ ] Implement `RuleExpressionEvaluator` in `strategy.core.pipeline.evaluator`.
- [ ] Add JSON Schema validation for rule definitions.
- [ ] Add comprehensive test suite verifying complex compound Boolean logic (AND, OR, NOT, CrossesAbove, CrossesBelow).
