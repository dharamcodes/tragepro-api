package com.tragepro.api.strategy.core.workflow.impl;

import com.tragepro.api.strategy.core.workflow.DataInitWorkflow;
import com.tragepro.api.strategy.core.workflow.activity.BaseActivity;
import com.tragepro.api.strategy.core.workflow.activity.DataInitActivity;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.Set;

public class DataInitWorkflowImpl implements DataInitWorkflow {

  private final DataInitActivity globalDataInitActivity =
      BaseActivity.globalActivity(DataInitActivity.class);
  private final DataInitActivity localDataInitActivity =
      BaseActivity.localActivity(DataInitActivity.class);

  @Override
  public Set<StrategyResponse> execute(String strategyName) {
    var strategyConfigLoadedResponse = localDataInitActivity.loadConfig(strategyName);
    var symbolDataLoadedResponse = localDataInitActivity.loadSymbol(strategyConfigLoadedResponse);
    var symbolDataStoredResponse = localDataInitActivity.storeData(symbolDataLoadedResponse);
    return globalDataInitActivity.run(symbolDataStoredResponse);
  }
}
