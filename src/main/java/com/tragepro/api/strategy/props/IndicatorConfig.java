package com.tragepro.api.strategy.props;

import com.tragepro.api.common.constant.IndicatorType;
import com.tragepro.api.common.constant.Timeframe;
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
