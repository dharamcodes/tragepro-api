package com.tragepro.api.datafeed.internal.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.MockDataFactory;
import com.tragepro.api.common.constant.DatafeedState;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.internal.context.DatafeedContext;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.service.CandleService;
import com.tragepro.api.datafeed.internal.service.WatchListService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DatafeedInitializerTest extends ContainerConfig {

  @Autowired private DatafeedInitializer datafeedInitializer;
  @Autowired private WatchListService watchListService;
  @Autowired private CandleService candleService;

  @Autowired private WatchlistContext watchlistContext;
  @Autowired private DatafeedContext datafeedContext;

  @Test
  void testDatafeedInitializerWithMockData() throws Exception {
    SymbolDataModel symbolData = MockDataFactory.createSymbolData("INIT_STOCK", "Init Corp");
    WatchListRequest wlRequest =
        MockDataFactory.createWatchListRequest("WL_INITIALIZER", symbolData);
    try {
      watchListService.create(wlRequest);
    } catch (Exception e) {
      // Ignored if already exists
    }

    CandleRequest candleReq =
        MockDataFactory.createCandleRequest("INIT_STOCK", "Init Corp", 1609459200000L);
    try {
      candleService.create(candleReq);
    } catch (Exception e) {
      // Ignored if already exists
    }

    datafeedInitializer.run();

    Set<SymbolDataModel> watchlistStocks = watchlistContext.getWatchlist("WL_INITIALIZER");
    assertNotNull(watchlistStocks);
    assertFalse(watchlistStocks.isEmpty());

    var datafeedModel = datafeedContext.get(symbolData);
    assertNotNull(datafeedModel);
    assertEquals(DatafeedState.INITIALIZED, datafeedModel.getState());
  }
}
