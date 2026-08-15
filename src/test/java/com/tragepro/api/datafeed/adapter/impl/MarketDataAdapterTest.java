package com.tragepro.api.datafeed.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketDataAdapterTest {

  @Mock private DatafeedService datafeedService;

  private DatafeedAdapterImpl datafeedAdapter;

  @BeforeEach
  void setUp() {
    datafeedAdapter = new DatafeedAdapterImpl(datafeedService);
  }

  @Test
  void testLoadData() {
    LoadCandleRequest request = new LoadCandleRequest("AAPL", 5);
    LoadCandleResponse expectedResponse =
        LoadCandleResponse.builder().watchList("AAPL").message("Loaded").build();
    when(datafeedService.loadData(request)).thenReturn(expectedResponse);

    LoadCandleResponse response = datafeedAdapter.loadData(request);

    assertNotNull(response);
    assertEquals("AAPL", response.watchList());
    verify(datafeedService).loadData(request);
  }
}
