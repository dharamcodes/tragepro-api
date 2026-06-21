package com.tragepro.api.strategy.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.strategy.model.StrategyBuilderModel;
import com.tragepro.api.strategy.model.StrategyEvaluatorModel;
import com.tragepro.api.strategy.model.StrategyExecutorModel;
import com.tragepro.api.strategy.model.StrategyStepsModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
// @Document(collection = "securityDetails")
public class StrategyEntity extends BaseEntity {
  private String id;
  private String name;
  private String symbol;
  private StrategyStepsModel steps;
  private StrategyBuilderModel builder;
  private StrategyEvaluatorModel evaluator;
  private StrategyExecutorModel executor;
  private List<CandleData> candleData;
}
