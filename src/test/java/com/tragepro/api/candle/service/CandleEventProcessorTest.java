package com.tragepro.api.candle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import com.tragepro.api.candle.event.CandleSavedEvent;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.repository.CandleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

class CandleEventProcessorTest {

    private CandleRepository candleRepository;
    private ApplicationEventPublisher eventPublisher;
    private CandleEventProcessor processor;

    @BeforeEach
    void setUp() {
        candleRepository = mock(CandleRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        processor = new CandleEventProcessor(candleRepository, eventPublisher);
    }

    @Test
    void testOnCandleReceived() {
        Symbol symbol = new Symbol("BTCUSD", "Bitcoin");
        Candle candle = new Candle(1700000000L, 100.0, 110.0, 90.0, 105.0, 50.0);
        CandleReceivedEvent event = new CandleReceivedEvent(symbol, candle);

        processor.onCandleReceived(event);

        verify(candleRepository, times(1)).save(any(CandleEntity.class));

        ArgumentCaptor<CandleSavedEvent> eventCaptor = ArgumentCaptor.forClass(CandleSavedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        CandleSavedEvent savedEvent = eventCaptor.getValue();
        assertNotNull(savedEvent);
        assertEquals("BTCUSD", savedEvent.candleSummaryResponse().getSymbolId());
        assertEquals(100.0, savedEvent.candleSummaryResponse().getOpen());
        assertEquals(105.0, savedEvent.candleSummaryResponse().getClose());
    }
}
