package com.tragepro.api.watchlist.service.impl;

import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.candle.service.CandleService;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.service.WatchlistMarketDataService;
import com.tragepro.api.watchlist.service.WatchlistService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchlistMarketDataServiceImpl implements WatchlistMarketDataService {

    private final WatchlistService watchlistService;
    private final CandleService candleService;

    @Override
    public List<WatchlistMarketDataResponse> getMarketDataForWatchlist(String watchlistId, String userId, Sort sort) {
        WatchlistResponse watchlist = watchlistService.getById(watchlistId, userId);

        if (watchlist.symbols() == null || watchlist.symbols().isEmpty()) {
            return List.of();
        }

        List<String> symbolsList = new ArrayList<>(watchlist.symbols());
        List<CandleSummaryResponse> latestCandles = candleService.getLatestForSymbols(symbolsList);

        List<WatchlistMarketDataResponse> marketData =
                latestCandles.stream().map(this::mapToMarketData).collect(java.util.stream.Collectors.toList());

        if (sort != null && sort.isSorted()) {
            Sort.Order order = sort.iterator().next(); // Simple single-property sort support for now
            Comparator<WatchlistMarketDataResponse> comparator = getComparator(order.getProperty());

            if (comparator != null) {
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                marketData.sort(comparator);
            }
        }

        return marketData;
    }

    private WatchlistMarketDataResponse mapToMarketData(CandleSummaryResponse candle) {
        return new WatchlistMarketDataResponse(
                candle.getSymbolId(),
                candle.getSymbolName(),
                candle.getOpen(),
                candle.getClose(),
                candle.getVolume(),
                candle.getClose() // Last traded price mapped to close price
                );
    }

    private Comparator<WatchlistMarketDataResponse> getComparator(String property) {
        return switch (property) {
            case "volume" -> Comparator.comparingDouble(WatchlistMarketDataResponse::volume);
            case "price", "lastTradedPrice", "close" -> Comparator.comparingDouble(
                    WatchlistMarketDataResponse::lastTradedPrice);
            case "symbolName" -> Comparator.comparing(WatchlistMarketDataResponse::symbolName);
            default -> null;
        };
    }
}
