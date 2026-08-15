package com.tragepro.api.strategy.core.props;

import com.tragepro.api.domain.datafeed.constant.Timeframe;
import com.tragepro.api.domain.strategy.constant.IndicatorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorConfig {
  private IndicatorType name;
  private Timeframe timeFrame;
}
