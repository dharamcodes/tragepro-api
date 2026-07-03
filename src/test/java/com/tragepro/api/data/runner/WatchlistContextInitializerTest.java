package com.tragepro.api.data.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.context.WatchlistContext;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.service.WatchListService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WatchlistContextInitializerTest {

  @Mock private WatchListService watchListService;
  @Mock private WatchlistContext watchlistContext;

  @InjectMocks private WatchlistContextInitializer initializer;

  @Test
  void testRun() {
    WatchListResponse watchlist = WatchListResponse.builder().name("WL1").stocks(Set.of()).build();
    when(watchListService.getAll()).thenReturn(Set.of(watchlist));

    assertDoesNotThrow(() -> initializer.run());

    verify(watchlistContext).addWatchlist("WL1", Set.of());
  }
}
