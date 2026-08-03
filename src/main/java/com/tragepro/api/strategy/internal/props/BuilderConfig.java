package com.tragepro.api.strategy.internal.props;

import com.tragepro.api.common.constant.TimeUnit;
import com.tragepro.api.strategy.internal.constant.StrategyBuilderStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuilderConfig {
  private StrategyBuilderStep name;
  private int time;
  private TimeUnit uom;
}
