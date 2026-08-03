package com.tragepro.api.strategy.internal.workflow.activity;

import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.strategy.dto.CandleModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BuilderActivity extends BaseActivity {

  @ActivityMethod(name = "BUILDER_ACTIVITY-RUN")
  StrategyResponse run(StrategyRequest strategyRequest);

  @ActivityMethod(name = "LOAD_BASE_CANDLE_DATA")
  StrategyResponse loadBaseCandleData(
      StrategyRequest strategyRequest, TimeframeModel baseTimeframe);

  @ActivityMethod(name = "CANDLE_TIMEFRAME_CONVERTER")
  StrategyResponse candleTimeframeConverter(CandleModel baseData, TimeframeModel timeframeModel);
}
