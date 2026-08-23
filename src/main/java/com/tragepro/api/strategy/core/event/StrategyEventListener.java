package com.tragepro.api.strategy.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StrategyEventListener {

    @ApplicationModuleListener
    public void on(StrategyEvent event) {
        log.info("Received StrategyEvent: {} - {}", event.eventId(), event.message());
    }
}
