package com.tragepro.api.datafeed.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.datafeed.core.context.DatafeedContext;
import com.tragepro.api.datafeed.core.feed.CandleIngestAdapter;
import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatafeedServiceImpl implements DatafeedService {

    private final WatchListService watchListService;
    private final SecurityService securityService;
    private final CandleIngestAdapter candleIngestAdapter;
    private final DatafeedContext datafeedContext;

    @Override
    public LoadCandleResponse loadData(LoadCandleRequest request) {
        validateRequest(request);

        WatchListResponse watchlist = findWatchlistByName(request.watchListName());
        Set<SymbolDataModel> stocks = watchlist.stocks();

        if (stocks == null || stocks.isEmpty()) {
            log.info("No stocks defined in watchlist :: {}", request.watchListName());
            return buildResponse(request.watchListName(), "No symbols found in watchlist to process");
        }

        try {
            loadWatchlistData(stocks, request.daysBack());
        } catch (Exception e) {
            log.error("Error loading stocks from watchlist :: {}", request.watchListName(), e);
            throw new AppException(ErrorType.INTERNAL_ERROR);
        }

        return buildResponse(request.watchListName(), "Data load initiated successfully");
    }

    private void validateRequest(LoadCandleRequest request) {
        if (request == null || request.watchListName() == null) {
            log.error("Invalid load request");
            throw new AppException(ErrorType.INVALID_PARAMETER);
        }
    }

    private WatchListResponse findWatchlistByName(String watchListName) {
        return watchListService.getAll().stream()
                .filter(w -> w.name().equalsIgnoreCase(watchListName))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Watchlist not found :: {}", watchListName);
                    return new AppException(ErrorType.DATA_NOT_FOUND);
                });
    }

    private void loadWatchlistData(Set<SymbolDataModel> stocks, int daysBack) {
        log.info("Starting data load for {} symbols with daysBack={}", stocks.size(), daysBack);
        stocks.forEach(stock -> processStockDataLoad(stock, daysBack));
        log.info("Data load process completed for all watchlist symbols");
    }

    private void processStockDataLoad(SymbolDataModel stock, int daysBack) {
        log.info("Processing data load for stock symbol: {}", stock.symbol());
        Optional<SecurityResponse> securityOpt = resolveSecurity(stock.symbol());
        if (securityOpt.isEmpty()) {
            return;
        }

        datafeedContext.transitionTo(stock, DatafeedState.PROCESSING);

        try {
            LocalDate latestDate = candleIngestAdapter.fetchAndIngest(securityOpt.get(), stock, daysBack);
            datafeedContext.transitionTo(stock, DatafeedState.COMPLETED, latestDate);
            log.info("Data load completed successfully for symbol: {}", stock.symbol());
        } catch (Exception ex) {
            log.error("Failed to ingest candle data for symbol: {}", stock.symbol(), ex);
        } finally {
            safeRevertToInitialized(stock);
        }
    }

    private Optional<SecurityResponse> resolveSecurity(String symbol) {
        try {
            return Optional.of(securityService.fetSecurityBySymbol(symbol));
        } catch (Exception ex) {
            log.warn("Security not found for symbol: {}, skipping", symbol, ex);
            return Optional.empty();
        }
    }

    private void safeRevertToInitialized(SymbolDataModel stock) {
        try {
            datafeedContext.transitionTo(stock, DatafeedState.INITIALIZED);
        } catch (Exception ex) {
            log.error("Failed to revert state for symbol: {}", stock.symbol(), ex);
        }
    }

    private LoadCandleResponse buildResponse(String watchListName, String message) {
        return LoadCandleResponse.builder()
                .watchList(watchListName)
                .message(message)
                .build();
    }
}
