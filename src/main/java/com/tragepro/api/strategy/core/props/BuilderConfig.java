package com.tragepro.api.strategy.core.props;

import com.tragepro.api.domain.datafeed.constant.TimeUnit;
import com.tragepro.api.domain.strategy.constant.StrategyBuilderStep;
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
