package com.tragepro.api.strategy.model;

import lombok.Data;

import java.util.List;

@Data
public class StrategySteps {
    private String name;
    private List<TimeframeConfig> timeframe;
    private List<String> builder;
    private List<String> evaluator;
    private List<String> executor;
}
