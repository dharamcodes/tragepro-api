package com.tragepro.api.strategy.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(StrategyEvent event) {
        log.info("Publishing StrategyEvent: {}", event.eventId());
        applicationEventPublisher.publishEvent(event);
    }
}
