package com.tragepro.api.candle.service;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import com.tragepro.api.candle.event.CandleSavedEvent;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.candle.repository.CandleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleEventProcessor {

    private final CandleRepository candleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @ApplicationModuleListener
    public void onCandleReceived(CandleReceivedEvent event) {
        log.debug("Processing received candle for symbol: {}", event.symbol().id());

        CandleEntity entity = new CandleEntity(null, event.symbol(), event.candle());
        candleRepository.save(entity);

        log.debug("Saved candle to database for symbol: {}", event.symbol().id());

        CandleSummaryResponse summaryResponse = CandleSummaryResponse.builder()
                .symbolId(event.symbol().id())
                .symbolName(event.symbol().name())
                .open(event.candle().open())
                .close(event.candle().close())
                .volume(event.candle().volume())
                .build();

        eventPublisher.publishEvent(new CandleSavedEvent(summaryResponse));
    }
}
