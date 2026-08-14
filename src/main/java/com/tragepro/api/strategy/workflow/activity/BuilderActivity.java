package com.tragepro.api.strategy.workflow.activity;

import com.tragepro.api.datafeed.model.TimeframeModel;
import com.tragepro.api.strategy.model.CandleModel;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BuilderActivity {

  @ActivityMethod(name = "BUILDER_ACTIVITY-RUN")
  StrategyResponse run(StrategyRequest strategyRequest);

  @ActivityMethod(name = "LOAD_BASE_CANDLE_DATA")
  StrategyResponse loadBaseCandleData(
      StrategyRequest strategyRequest, TimeframeModel baseTimeframe);

  @ActivityMethod(name = "CANDLE_TIMEFRAME_CONVERTER")
  StrategyResponse candleTimeframeConverter(CandleModel baseData, TimeframeModel timeframeModel);
}
