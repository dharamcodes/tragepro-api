package com.tragepro.api.strategy.props;

import com.tragepro.api.datafeed.constant.TimeUnit;
import com.tragepro.api.datafeed.constant.Timeframe;
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
