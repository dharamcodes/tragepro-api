package com.tragepro.api.strategy.dto;

import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.constant.StrategyStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusModel {
  private StrategyState state;
  private StrategyStep step;
}
