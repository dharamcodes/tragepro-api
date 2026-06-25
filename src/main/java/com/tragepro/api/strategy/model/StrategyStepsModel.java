package com.tragepro.api.strategy.model;

import com.tragepro.api.strategy.constant.StrategyNameType;
import com.tragepro.api.strategy.core.StrategyBuilder;
import com.tragepro.api.strategy.core.StrategyEvaluator;
import com.tragepro.api.strategy.core.StrategyExecutor;
import java.util.List;
import lombok.Data;

@Data
public class StrategyStepsModel {
  private StrategyNameType name;
  private List<TimeframeModel> timeframe;
  private List<StrategyBuilder> builder;
  private List<StrategyEvaluator> evaluator;
  private List<StrategyExecutor> executor;
}
