package com.tragepro.api.domain.strategy;

import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.constant.Timeframe;
import com.tragepro.api.domain.strategy.constant.IndicatorType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorModel {
  private IndicatorType name;
  private Timeframe timeFrame;
  private Set<CandleDataModel> levels;
}
