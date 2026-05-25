package com.tragepro.api.watchlist.service;

import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import java.util.List;

public interface WatchlistService {
    WatchlistResponse create(String userId, WatchlistRequest request);

    List<WatchlistResponse> getAllForUser(String userId);

    WatchlistResponse getById(String id, String userId);

    WatchlistResponse update(String id, String userId, WatchlistRequest request);

    void delete(String id, String userId);

    WatchlistResponse addSymbol(String id, String userId, String symbolId);

    WatchlistResponse removeSymbol(String id, String userId, String symbolId);
}
