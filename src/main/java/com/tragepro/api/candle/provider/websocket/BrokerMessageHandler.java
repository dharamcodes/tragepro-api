package com.tragepro.api.candle.provider.websocket;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import org.springframework.web.socket.WebSocketSession;

public interface BrokerMessageHandler {
    /**
     * Determines if this handler supports the given broker name.
     */
    boolean supports(String brokerName);

    /**
     * Parses the raw message and returns a CandleReceivedEvent.
     */
    CandleReceivedEvent handleMessage(WebSocketSession session, String message);
}
