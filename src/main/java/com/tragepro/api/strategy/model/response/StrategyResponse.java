package com.tragepro.api.strategy.model.response;

import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.strategy.model.StrategyBuilderModel;
import com.tragepro.api.strategy.model.StrategyEvaluatorModel;
import com.tragepro.api.strategy.model.StrategyExecutorModel;
import com.tragepro.api.strategy.model.StrategyStepsModel;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StrategyResponse {
  private String id;
  private String name;
  private String symbol;
  private StrategyStepsModel steps;
  private StrategyBuilderModel builder;
  private StrategyEvaluatorModel evaluator;
  private StrategyExecutorModel executor;
  private List<CandleData> candleData;
}
