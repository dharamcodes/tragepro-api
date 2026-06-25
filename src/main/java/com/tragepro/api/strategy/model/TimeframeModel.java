package com.tragepro.api.strategy.model;

import com.tragepro.api.strategy.constant.TimeFrameType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeframeModel {
  private int value;
  private String uom;
  private TimeFrameType type;
}
