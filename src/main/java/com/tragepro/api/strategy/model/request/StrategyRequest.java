package com.tragepro.api.strategy.model.request;

import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.strategy.model.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRequest {
  private StrategyModel strategy;
  private SymbolModel symbolData;
  private CandleModel candleData;
  private StatusModel currentState;
  private Set<IndicatorModel> indicators;
  private Set<TimeframeModel> timeframes;
}
