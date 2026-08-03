package com.tragepro.api.datafeed.internal.context;

import com.tragepro.api.common.model.SymbolDataModel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class WatchlistContext {

  private final Map<String, Set<SymbolDataModel>> watchlist = new HashMap<>();

  public Set<SymbolDataModel> getWatchlist(String name) {
    return watchlist.getOrDefault(name, new HashSet<>());
  }

  public void addWatchlist(String name, Set<SymbolDataModel> symbols) {
    watchlist.put(name, symbols);
  }
}
