package com.tragepro.api.strategy.internal.workflow;

import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.internal.workflow.activity.BaseActivity;
import com.tragepro.api.strategy.internal.workflow.activity.DataInitActivity;
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
