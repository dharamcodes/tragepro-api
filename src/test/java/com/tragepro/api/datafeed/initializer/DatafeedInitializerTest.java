package com.tragepro.api.datafeed.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.context.DatafeedContext;
import com.tragepro.api.common.context.WatchlistContext;
import com.tragepro.api.datafeed.model.response.WatchListResponse;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.WatchListService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatafeedInitializerTest {

  @Mock private WatchListService watchListService;
  @Mock private WatchlistContext watchlistContext;
  @Mock private CandleService candleService;
  @Mock private DatafeedContext datafeedContext;

  @InjectMocks private DatafeedInitializer initializer;

  @Test
  void testRun() {
    WatchListResponse watchlist = WatchListResponse.builder().name("WL1").stocks(Set.of()).build();
    when(watchListService.getAll()).thenReturn(Set.of(watchlist));
    when(candleService.getLatestCandlesBySymbols(any())).thenReturn(Set.of());

    assertDoesNotThrow(() -> initializer.run());

    verify(watchlistContext).addWatchlist("WL1", Set.of());
  }
}
