package com.tragepro.api.domain.strategy.response;

import com.tragepro.api.domain.datafeed.TimeframeModel;
import com.tragepro.api.domain.strategy.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyResponse {
  private StrategyModel strategy;
  private SymbolModel symbolData;
  private CandleModel candleData;
  private StatusModel currentState;
  private Set<IndicatorModel> indicators;
  private Set<TimeframeModel> timeframes;
}
