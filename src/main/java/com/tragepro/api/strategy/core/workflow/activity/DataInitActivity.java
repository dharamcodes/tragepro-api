package com.tragepro.api.strategy.core.workflow.activity;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.Set;

@ActivityInterface
public interface DataInitActivity {

    @ActivityMethod(name = "DATA_INIT_ACTIVITY-RUN")
    Set<StrategyResponse> run(Set<StrategyRequest> strategyRequests);

    @ActivityMethod(name = "LOAD_CONFIG")
    StrategyRequest loadConfig(String strategyName);

    @ActivityMethod(name = "LOAD_SYMBOL")
    Set<StrategyRequest> loadSymbol(StrategyRequest strategyRequest);

    @ActivityMethod(name = "STORE_DATA")
    Set<StrategyRequest> storeData(Set<StrategyRequest> strategyRequests);
}
