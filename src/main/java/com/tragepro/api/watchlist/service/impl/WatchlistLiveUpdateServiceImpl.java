package com.tragepro.api.watchlist.service.impl;

import com.tragepro.api.candle.event.CandleSavedEvent;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.repository.WatchlistRepository;
import com.tragepro.api.watchlist.service.WatchlistLiveUpdateService;
import com.tragepro.api.watchlist.service.WatchlistService;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchlistLiveUpdateServiceImpl implements WatchlistLiveUpdateService {

    private final WatchlistService watchlistService;
    private final WatchlistRepository watchlistRepository;

    // Map of watchlistId -> Set of SseEmitters
    private final Map<String, Set<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(String watchlistId, String userId) {
        // Validate user has access to watchlist
        WatchlistResponse watchlist = watchlistService.getById(watchlistId, userId);

        SseEmitter emitter = new SseEmitter(300_000L); // 5 minutes timeout

        emittersMap
                .computeIfAbsent(watchlistId, k -> new CopyOnWriteArraySet<>())
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(watchlistId, emitter));
        emitter.onTimeout(() -> removeEmitter(watchlistId, emitter));
        emitter.onError((e) -> removeEmitter(watchlistId, emitter));

        return emitter;
    }

    private void removeEmitter(String watchlistId, SseEmitter emitter) {
        Set<SseEmitter> emitters = emittersMap.get(watchlistId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                emittersMap.remove(watchlistId);
            }
        }
    }

    @ApplicationModuleListener
    public void onCandleSaved(CandleSavedEvent event) {
        CandleSummaryResponse candle = event.candleSummaryResponse();

        WatchlistMarketDataResponse marketData = new WatchlistMarketDataResponse(
                candle.getSymbolId(),
                candle.getSymbolName(),
                candle.getOpen(),
                candle.getClose(),
                candle.getVolume(),
                candle.getClose());

        // Find which watchlists contain this symbol and have active emitters
        emittersMap.forEach((watchlistId, emitters) -> {
            try {
                Optional<WatchlistEntity> entityOpt = watchlistRepository.findById(watchlistId);
                if (entityOpt.isEmpty() || !entityOpt.get().getSymbols().contains(candle.getSymbolId())) {
                    return; // Skip if watchlist doesn't contain the symbol
                }

                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(
                                SseEmitter.event().name("market-data-update").data(marketData));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        removeEmitter(watchlistId, emitter);
                    }
                }
            } catch (Exception e) {
                log.error("Error broadcasting to watchlist {}", watchlistId, e);
            }
        });
    }
}
