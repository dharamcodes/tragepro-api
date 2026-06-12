package com.tragepro.api.data.client;

import com.tragepro.api.data.client.helper.DataSocketHelper;
import com.tragepro.api.data.model.SocketConfig;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Component
public class DataFeedSocket extends AbstractWebSocketHandler {

    private final WebSocketClient webSocketClient;
    private final SocketConfig properties;
    private final DataSocketHelper helper;

    private WebSocketSession session;
    private String currentUrl;
    private final AtomicBoolean isConnecting = new AtomicBoolean(false);

    @Autowired
    public DataFeedSocket(SocketConfig properties, DataSocketHelper helper) {
        this.webSocketClient = new StandardWebSocketClient();
        this.properties = properties;
        this.helper = helper;
    }

    // Constructor for testing / dependency injection
    public DataFeedSocket(WebSocketClient webSocketClient, SocketConfig properties, DataSocketHelper helper) {
        this.webSocketClient = webSocketClient;
        this.properties = properties;
        this.helper = helper;
    }

    public CompletableFuture<WebSocketSession> connect() {
        String url = helper.buildConnectionUrl(properties);
        return connect(url);
    }

    public CompletableFuture<WebSocketSession> connect(String url) {
        if (isConnecting.get() || isConnected()) {
            CompletableFuture<WebSocketSession> future = new CompletableFuture<>();
            if (session != null) {
                future.complete(session);
            } else {
                future.completeExceptionally(
                        new IllegalStateException("Connection is already in progress or connected"));
            }
            return future;
        }

        this.currentUrl = url;
        helper.setAutoReconnect(properties.isReconnect());
        this.isConnecting.set(true);
        log.info("Connecting to WebSocket URL: {}", url);

        CompletableFuture<WebSocketSession> future = new CompletableFuture<>();
        try {
            webSocketClient.execute(this, url).whenComplete((s, throwable) -> {
                isConnecting.set(false);
                if (throwable != null) {
                    log.error("Failed to establish WebSocket connection to: {}", url, throwable);
                    future.completeExceptionally(throwable);
                    helper.triggerReconnect(this, url);
                } else {
                    this.session = s;
                    helper.resetReconnectAttempts();
                    future.complete(s);
                }
            });
        } catch (Exception e) {
            isConnecting.set(false);
            log.error("Failed to execute WebSocket connection for URL: {}", url, e);
            future.completeExceptionally(e);
            helper.triggerReconnect(this, url);
        }
        return future;
    }

    public void disconnect() {
        helper.setAutoReconnect(false);
        if (session != null && session.isOpen()) {
            try {
                log.info("Disconnecting WebSocket session: {}", session.getId());
                session.close();
            } catch (IOException e) {
                log.error("Error closing WebSocket session", e);
            }
        }
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    public void sendText(String text) {
        if (!isConnected()) {
            throw new IllegalStateException("WebSocket is not connected");
        }
        try {
            session.sendMessage(new TextMessage(text));
        } catch (IOException e) {
            log.error("Error sending text message", e);
            helper.dispatchError(e);
        }
    }

    public void sendBinary(ByteBuffer payload) {
        if (!isConnected()) {
            throw new IllegalStateException("WebSocket is not connected");
        }
        try {
            session.sendMessage(new BinaryMessage(payload));
        } catch (IOException e) {
            log.error("Error sending binary message", e);
            helper.dispatchError(e);
        }
    }

    public void addListener(SocketFeedListener listener) {
        helper.addListener(listener);
    }

    public void removeListener(SocketFeedListener listener) {
        helper.removeListener(listener);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
        log.info("WebSocket connection established. Session ID: {}", session.getId());
        helper.dispatchOpen(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Received text message: {}", message.getPayload());
        helper.dispatchTextMessage(message.getPayload());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.debug("Received binary message: {} bytes", message.getPayloadLength());
        ByteBuffer payload = message.getPayload();
        helper.dispatchBinaryMessage(payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket transport error: ", exception);
        helper.dispatchError(exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WebSocket connection closed. Session ID: {}, Status: {}", session.getId(), status);
        this.session = null;
        helper.dispatchClose(status);
        if (helper.isAutoReconnect()) {
            helper.triggerReconnect(this, currentUrl);
        }
    }

    @PreDestroy
    public void shutdown() {
        disconnect();
    }
}
