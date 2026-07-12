package com.tragepro.api.strategy.model.entity;

import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.common.model.entity.BaseEntity;
import com.tragepro.api.strategy.model.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "strategy")
public class StrategyEntity extends BaseEntity {
  @Id private String id;
  private StrategyModel strategy;
  private SymbolModel symbolData;
  private CandleModel candleData;
  private StatusModel currentState;
  private Set<IndicatorModel> indicators;
  private Set<TimeframeModel> timeframes;
}
