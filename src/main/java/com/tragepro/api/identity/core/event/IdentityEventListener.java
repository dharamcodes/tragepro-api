package com.tragepro.api.identity.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdentityEventListener {

    @ApplicationModuleListener
    public void on(IdentityEvent event) {
        log.info("Received IdentityEvent: {} - {}", event.eventId(), event.message());
        // Handle the event
    }
}
