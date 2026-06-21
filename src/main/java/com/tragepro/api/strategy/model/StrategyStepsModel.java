package com.tragepro.api.strategy.model;

import com.tragepro.api.strategy.builder.AbstractStrategyBuilder;
import com.tragepro.api.strategy.constant.StrategyNameType;
import com.tragepro.api.strategy.evaluator.StrategyEvaluator;
import com.tragepro.api.strategy.executor.StrategyExecutor;
import java.util.List;
import lombok.Data;

@Data
public class StrategyStepsModel {
  private StrategyNameType name;
  private List<TimeframeModel> timeframe;
  private List<AbstractStrategyBuilder> builder;
  private List<StrategyEvaluator> evaluator;
  private List<StrategyExecutor> executor;
}
