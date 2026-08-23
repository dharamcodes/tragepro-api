package com.tragepro.api.journal.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JournalEventListener {

    @ApplicationModuleListener
    public void on(JournalEvent event) {
        log.info("Received JournalEvent: {} - {}", event.eventId(), event.message());
    }
}
