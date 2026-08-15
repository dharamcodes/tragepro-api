package com.tragepro.api.strategy.core.props;

import com.tragepro.api.domain.strategy.constant.StrategyExecutorStep;
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
