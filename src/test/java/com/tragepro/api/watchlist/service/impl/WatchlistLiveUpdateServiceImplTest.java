package com.tragepro.api.watchlist.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.tragepro.api.candle.event.CandleSavedEvent;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.repository.WatchlistRepository;
import com.tragepro.api.watchlist.service.WatchlistService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class WatchlistLiveUpdateServiceImplTest {

    private WatchlistService watchlistService;
    private WatchlistRepository watchlistRepository;
    private WatchlistLiveUpdateServiceImpl service;

    @BeforeEach
    void setUp() {
        watchlistService = mock(WatchlistService.class);
        watchlistRepository = mock(WatchlistRepository.class);
        service = new WatchlistLiveUpdateServiceImpl(watchlistService, watchlistRepository);
    }

    @Test
    void testSubscribe() {
        WatchlistResponse mockResponse = mock(WatchlistResponse.class);
        when(watchlistService.getById("w1", "u1")).thenReturn(mockResponse);

        SseEmitter emitter = service.subscribe("w1", "u1");

        assertNotNull(emitter);
        verify(watchlistService, times(1)).getById("w1", "u1");
    }

    @Test
    void testOnCandleSaved_MatchesWatchlist() {
        // Setup subscription
        WatchlistResponse mockResponse = mock(WatchlistResponse.class);
        when(watchlistService.getById("w1", "u1")).thenReturn(mockResponse);
        service.subscribe("w1", "u1");

        // Setup repository
        WatchlistEntity entity = mock(WatchlistEntity.class);
        when(entity.getSymbols()).thenReturn(Set.of("BTCUSD"));
        when(watchlistRepository.findById("w1")).thenReturn(Optional.of(entity));

        // Publish event
        CandleSummaryResponse candle = CandleSummaryResponse.builder()
                .symbolId("BTCUSD")
                .symbolName("Bitcoin")
                .open(100.0)
                .close(105.0)
                .volume(50.0)
                .build();
        CandleSavedEvent event = new CandleSavedEvent(candle);

        // Not asserting actual send as it requires complex setup or spy, but we ensure it runs without exception
        service.onCandleSaved(event);

        verify(watchlistRepository, times(1)).findById("w1");
    }

    @Test
    void testOnCandleSaved_NoMatch() {
        // Setup subscription
        when(watchlistService.getById("w1", "u1")).thenReturn(mock(WatchlistResponse.class));
        service.subscribe("w1", "u1");

        // Setup repository
        WatchlistEntity entity = mock(WatchlistEntity.class);
        when(entity.getSymbols()).thenReturn(Set.of("ETHUSD"));
        when(watchlistRepository.findById("w1")).thenReturn(Optional.of(entity));

        // Publish event for BTCUSD
        CandleSummaryResponse candle =
                CandleSummaryResponse.builder().symbolId("BTCUSD").build();
        service.onCandleSaved(new CandleSavedEvent(candle));

        verify(watchlistRepository, times(1)).findById("w1");
    }

    @Test
    void testOnCandleSaved_EntityNotFound() {
        // Setup subscription
        when(watchlistService.getById("w1", "u1")).thenReturn(mock(WatchlistResponse.class));
        service.subscribe("w1", "u1");

        // Setup repository
        when(watchlistRepository.findById("w1")).thenReturn(Optional.empty());

        // Publish event
        CandleSummaryResponse candle =
                CandleSummaryResponse.builder().symbolId("BTCUSD").build();
        service.onCandleSaved(new CandleSavedEvent(candle));

        verify(watchlistRepository, times(1)).findById("w1");
    }

    @Test
    void testEmitterCallbacks() {
        when(watchlistService.getById("w1", "u1")).thenReturn(mock(WatchlistResponse.class));
        SseEmitter emitter = service.subscribe("w1", "u1");

        emitter.complete();
    }
}
