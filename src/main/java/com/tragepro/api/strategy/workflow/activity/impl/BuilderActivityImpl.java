package com.tragepro.api.strategy.workflow.activity.impl;

import com.tragepro.api.common.workflow.BaseActivity;
import com.tragepro.api.datafeed.model.TimeframeModel;
import com.tragepro.api.strategy.model.CandleModel;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.workflow.activity.BuilderActivity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuilderActivityImpl extends BaseActivity implements BuilderActivity {

  @Override
  public List<Class<?>> globalActivities() {
    return List.of(BuilderActivity.class);
  }

  @Override
  public List<Class<?>> localActivities() {
    return List.of();
  }

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
