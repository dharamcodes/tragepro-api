package com.tragepro.api.strategy.props;

import com.tragepro.api.strategy.constant.StrategyExecutorStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorConfig {
  private StrategyExecutorStep name;
}
