package com.tragepro.api.watchlist.model.response;

import java.util.Set;

public record WatchlistResponse(String id, String name, Set<String> symbols) {}
