package com.tragepro.api.candle.provider.websocket;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class BrokerWebSocketClient extends TextWebSocketHandler {

    private final String brokerName;
    private final List<BrokerMessageHandler> messageHandlers;
    private final ApplicationEventPublisher eventPublisher;

    private BrokerMessageHandler getHandler() {
        return messageHandlers.stream()
                .filter(handler -> handler.supports(brokerName))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("No BrokerMessageHandler found for broker: " + brokerName));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Connected to broker: {}", brokerName);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Received message from broker {}: {}", brokerName, message.getPayload());
        BrokerMessageHandler handler = getHandler();
        CandleReceivedEvent event = handler.handleMessage(session, message.getPayload());

        if (event != null) {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error on broker {}: {}", brokerName, exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed for broker {}: {}", brokerName, status);
        // Note: Reconnection logic could be added here or via a scheduled task
    }
}
