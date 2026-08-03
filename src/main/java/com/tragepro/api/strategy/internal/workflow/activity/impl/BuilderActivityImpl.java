package com.tragepro.api.strategy.internal.workflow.activity.impl;

import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.strategy.dto.CandleModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.internal.workflow.activity.BuilderActivity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuilderActivityImpl implements BuilderActivity {

  @Override
  public StrategyResponse run(StrategyRequest strategyRequest) {
    return StrategyResponse.builder().build();
  }

  @Override
  public StrategyResponse loadBaseCandleData(
      StrategyRequest strategyRequest, TimeframeModel baseTimeframe) {
    return null;
  }

  @Override
  public StrategyResponse candleTimeframeConverter(
      CandleModel baseData, TimeframeModel timeframeModel) {
    return null;
  }
}
