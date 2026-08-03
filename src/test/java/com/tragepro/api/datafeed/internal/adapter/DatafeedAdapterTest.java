package com.tragepro.api.datafeed.internal.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.DatafeedAdapter;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.CandleResponse;
import com.tragepro.api.datafeed.dto.SecurityResponse;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.entity.SecurityEntity;
import com.tragepro.api.datafeed.internal.repository.SecurityRepository;
import com.tragepro.api.datafeed.internal.service.CandleService;
import com.tragepro.api.datafeed.internal.service.WatchListService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DatafeedAdapterTest extends ContainerConfig {

  @Autowired private DatafeedAdapter datafeedAdapter;
  @Autowired private WatchListService watchListService;
  @Autowired private CandleService candleService;
  @Autowired private SecurityRepository securityRepository;
  @Autowired private WatchlistContext watchlistContext;

  @Test
  void testDatafeedAdapterInterModuleMethods() {
    WatchListRequest wlReq =
        WatchListRequest.builder()
            .name("WL_FACADE")
            .stocks(Set.of(new SymbolDataModel("MSFT", "Microsoft")))
            .build();
    watchListService.create(wlReq);
    watchlistContext.addWatchlist("WL_FACADE", wlReq.stocks());

    Set<SymbolDataModel> symbols = datafeedAdapter.getWatchlistSymbols("WL_FACADE");
    assertNotNull(symbols);

    CandleRequest cReq =
        new CandleRequest(
            new SymbolDataModel("MSFT", "Microsoft"),
            new com.tragepro.api.common.model.CandleDataModel(
                1609459200000L, 100.0, 105.0, 99.0, 104.0, 1000L));
    candleService.create(cReq);

    List<CandleResponse> candles = datafeedAdapter.getCandlesBySymbolAndDaysBack("Microsoft", 10);
    assertNotNull(candles);

    Set<CandleResponse> latest = datafeedAdapter.getLatestCandlesBySymbols(Set.of("Microsoft"));
    assertNotNull(latest);

    securityRepository.deleteAll();
    SecurityEntity sec = new SecurityEntity();
    sec.setSymbol("MSFT");
    sec.setName("Microsoft");
    sec.setSecurityId(303);
    securityRepository.save(sec);

    SecurityResponse secResp = datafeedAdapter.getSecurityBySymbol("MSFT");
    assertEquals("MSFT", secResp.symbol());
  }
}
