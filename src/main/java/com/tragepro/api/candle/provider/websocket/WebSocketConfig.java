package com.tragepro.api.candle.provider.websocket;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {

    @Value("${broker.websocket.url:}")
    private String brokerUrl;

    @Value("${broker.websocket.token:}")
    private String token;

    @Value("${broker.websocket.name:generic}")
    private String brokerName;

    private final List<BrokerMessageHandler> messageHandlers;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        if (brokerUrl == null || brokerUrl.isBlank()) {
            log.info("Broker WebSocket URL is not configured. Skipping WebSocket client initialization.");
            return;
        }

        String urlWithToken = brokerUrl.contains("?") ? brokerUrl + "&token=" + token : brokerUrl + "?token=" + token;

        BrokerWebSocketClient clientHandler = new BrokerWebSocketClient(brokerName, messageHandlers, eventPublisher);
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();

        WebSocketConnectionManager connectionManager =
                new WebSocketConnectionManager(standardWebSocketClient, clientHandler, urlWithToken);
        connectionManager.setAutoStartup(true);
        connectionManager.start();

        log.info("Initialized WebSocket connection to broker: {}", brokerName);
    }
}
