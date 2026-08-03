package com.tragepro.api.strategy.internal.workflow.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.service.WatchListService;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.workflow.activity.impl.DataInitActivityImpl;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DataInitActivityTest extends ContainerConfig {

  @Autowired private DataInitActivityImpl dataInitActivity;
  @Autowired private WatchlistContext watchlistContext;
  @Autowired private StrategyContext strategyContext;
  @Autowired private WatchListService watchListService;

  @Test
  void testFullDataInitActivityFlow() {
    // 1. Populate Watchlist Context
    Set<SymbolDataModel> stocks =
        Set.of(new SymbolDataModel("AAPL", "Apple Inc."), new SymbolDataModel("MSFT", "Microsoft"));
    watchlistContext.addWatchlist("NIFTY_50", stocks);

    // 2. Load Config
    StrategyRequest configReq = dataInitActivity.loadConfig("INTRADAY_VP_VWAP");
    assertNotNull(configReq);
    assertNotNull(configReq.getStrategy());
    assertNotNull(configReq.getIndicators());
    assertNotNull(configReq.getTimeframes());
    assertFalse(configReq.getIndicators().isEmpty());
    assertFalse(configReq.getTimeframes().isEmpty());

    // 3. Load Symbol
    Set<StrategyRequest> symbolRequests = dataInitActivity.loadSymbol(configReq);
    assertNotNull(symbolRequests);
    assertEquals(2, symbolRequests.size());

    // 4. Store Data
    Set<StrategyRequest> storedRequests = dataInitActivity.storeData(symbolRequests);
    assertNotNull(storedRequests);
    assertEquals(2, storedRequests.size());

    // 5. Run DataInit Activity
    Set<StrategyResponse> runResponses = dataInitActivity.run(storedRequests);
    assertNotNull(runResponses);
    assertEquals(2, runResponses.size());

    // 6. Verify Context updated
    assertNotNull(strategyContext.get("INTRADAY_VP_VWAP"));
  }
}
