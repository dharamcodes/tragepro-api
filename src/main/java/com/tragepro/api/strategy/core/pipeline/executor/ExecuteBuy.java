package com.tragepro.api.strategy.core.pipeline.executor;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.pipeline.StrategyExecutor;
import org.springframework.stereotype.Component;

@Component("EXECUTE_BUY")
public class ExecuteBuy implements StrategyExecutor {

    @Override
    public StrategyResponse execute(StrategyRequest strategyRequest) {
        return null;
    }
}
