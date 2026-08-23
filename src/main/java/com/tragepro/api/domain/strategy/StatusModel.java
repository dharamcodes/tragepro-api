package com.tragepro.api.domain.strategy;

import com.tragepro.api.domain.strategy.constant.StrategyState;
import com.tragepro.api.domain.strategy.constant.StrategyStep;
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
