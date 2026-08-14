package com.tragepro.api.datafeed.model;

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
public class TimeframeModel {
  private int value;
  private TimeUnit uom;
  private Timeframe type;
}
