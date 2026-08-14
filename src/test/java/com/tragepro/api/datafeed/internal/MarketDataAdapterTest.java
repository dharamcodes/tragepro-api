package com.tragepro.api.datafeed.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.response.CandleResponse;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketDataAdapterTest {

  @Mock private CandleService candleService;

  @Mock private DatafeedService datafeedService;

  @Mock private WatchListService watchListService;

  private MarketDataAdapterImpl marketDataAdapter;

  @BeforeEach
  void setUp() {
    marketDataAdapter = new MarketDataAdapterImpl(candleService, datafeedService, watchListService);
  }

  @Test
  void testGetLatestCandles() {
    CandleResponse candleResponse = CandleResponse.builder().id("1").build();
    when(candleService.getLatestCandlesBySymbols(any())).thenReturn(Set.of(candleResponse));

    List<CandleResponse> result = marketDataAdapter.getLatestCandles(List.of("AAPL"));

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("1", result.get(0).id());
    verify(candleService).getLatestCandlesBySymbols(Set.of("AAPL"));
  }

  @Test
  void testGetLatestCandlesNullSymbols() {
    when(candleService.getLatestCandlesBySymbols(Set.of())).thenReturn(Set.of());

    List<CandleResponse> result = marketDataAdapter.getLatestCandles(null);

    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void testLoadData() {
    LoadCandleRequest request = new LoadCandleRequest("AAPL", 5);
    LoadCandleResponse expectedResponse =
        LoadCandleResponse.builder().watchList("AAPL").message("Loaded").build();
    when(datafeedService.loadData(request)).thenReturn(expectedResponse);

    LoadCandleResponse response = marketDataAdapter.loadData(request);

    assertNotNull(response);
    assertEquals("AAPL", response.watchList());
    verify(datafeedService).loadData(request);
  }
}
