package com.tragepro.api.strategy.core.props;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyConfig {
    private String name;
    private String desc;
    private String watchList;
    private List<TimeframeConfig> timeframes;
    private Set<IndicatorConfig> indicators;
    private StepsConfig strategySteps;
}
