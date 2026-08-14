package com.tragepro.api.datafeed.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tragepro.api.datafeed.model.SymbolDataModel;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WatchlistContextTest {

  private WatchlistContext watchlistContext;

  @BeforeEach
  void setUp() {
    watchlistContext = new WatchlistContext();
  }

  @Test
  void testAddAndGetWatchlist() {
    SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
    watchlistContext.addWatchlist("Tech", Set.of(symbol));

    Set<SymbolDataModel> result = watchlistContext.getWatchlist("Tech");
    assertEquals(1, result.size());
    assertTrue(result.contains(symbol));
  }

  @Test
  void testGetWatchlist_NonExistent_ReturnsEmptySet() {
    Set<SymbolDataModel> result = watchlistContext.getWatchlist("NonExistent");
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
