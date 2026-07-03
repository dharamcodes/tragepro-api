package com.tragepro.api.common.context;

import com.tragepro.api.common.model.SymbolData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class WatchlistContext {

  private final Map<String, Set<SymbolData>> watchlist = new HashMap<>();

  public Set<SymbolData> getWatchlist(String name) {
    return watchlist.getOrDefault(name, new HashSet<>());
  }

  public void addWatchlist(String name, Set<SymbolData> symbols) {
    watchlist.put(name, symbols);
  }
}
