package com.tragepro.api.strategy.core.props;

import com.tragepro.api.domain.datafeed.constant.TimeUnit;
import com.tragepro.api.domain.datafeed.constant.Timeframe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeframeConfig {
  private int value;
  private TimeUnit uom;
  private Timeframe type;
}
