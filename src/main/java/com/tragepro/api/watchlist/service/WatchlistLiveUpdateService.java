package com.tragepro.api.watchlist.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface WatchlistLiveUpdateService {

    /**
     * Subscribe to live updates for a specific watchlist.
     * @param watchlistId the watchlist ID
     * @param userId the user ID to ensure authorization (optional for basic impl)
     * @return SseEmitter instance
     */
    SseEmitter subscribe(String watchlistId, String userId);
}
