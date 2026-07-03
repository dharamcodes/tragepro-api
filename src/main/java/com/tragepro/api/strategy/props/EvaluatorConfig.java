package com.tragepro.api.strategy.props;

import com.tragepro.api.strategy.constant.StrategyEvaluatorStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluatorConfig {
  private StrategyEvaluatorStep name;
  private double probability;
}
