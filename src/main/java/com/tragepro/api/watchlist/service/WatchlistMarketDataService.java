package com.tragepro.api.watchlist.service;

import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface WatchlistMarketDataService {
    List<WatchlistMarketDataResponse> getMarketDataForWatchlist(String watchlistId, String userId, Sort sort);
}
