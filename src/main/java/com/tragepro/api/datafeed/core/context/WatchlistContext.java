package com.tragepro.api.datafeed.core.context;

import com.tragepro.api.domain.datafeed.SymbolDataModel;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class WatchlistContext {

  private final Map<String, Set<SymbolDataModel>> watchlist = new ConcurrentHashMap<>();

  /** Returns an unmodifiable view of the watchlist to prevent external mutation. */
  public Set<SymbolDataModel> getWatchlist(String name) {
    return Collections.unmodifiableSet(watchlist.getOrDefault(name, Set.of()));
  }

  public void addWatchlist(String name, Set<SymbolDataModel> symbols) {
    Set<SymbolDataModel> set = ConcurrentHashMap.newKeySet();
    if (symbols != null) {
      set.addAll(symbols);
    }
    watchlist.put(name, set);
  }
}
