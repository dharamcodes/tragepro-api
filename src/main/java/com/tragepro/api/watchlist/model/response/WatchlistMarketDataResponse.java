package com.tragepro.api.watchlist.model.response;

public record WatchlistMarketDataResponse(
        String symbolId, String symbolName, double open, double close, double volume, double lastTradedPrice) {}
