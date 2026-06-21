package com.tragepro.api.strategy.model;

import com.tragepro.api.strategy.constant.TimeFrameType;
import lombok.Data;

@Data
public class TimeframeModel {
  private int value;
  private String uom;
  private TimeFrameType type;
}
