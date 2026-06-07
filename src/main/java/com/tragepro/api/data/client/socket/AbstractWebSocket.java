package com.tragepro.api.data.client.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.data.client.socket.constant.ConnectionState;
import com.tragepro.api.data.model.request.WebSocketRequest;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * Base abstract WebSocket class providing state management, observer notifications,
 * and automatic retry reconnection with exponential backoff.
 *
 * @param <T> the type of WebSocketRequest sent by this client
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractWebSocket<T extends WebSocketRequest> extends AbstractWebSocketHandler {

    protected final WebSocketClient webSocketClient;
    protected final ObjectMapper objectMapper;
    protected volatile WebSocketSession session;

    private final AtomicReference<ConnectionState> connectionState =
            new AtomicReference<>(ConnectionState.DISCONNECTED);

    // Observer Pattern: Listeners registered to receive WebSocket events
    private final List<WebSocketListener> listeners = new CopyOnWriteArrayList<>();

    protected List<WebSocketListener> getListeners() {
        return listeners;
    }

    // Reconnection executor service
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "websocket-reconnect-thread");
        thread.setDaemon(true);
        return thread;
    });

    private volatile String currentUrl;
    private volatile boolean autoReconnect = true;
    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final long INITIAL_RECONNECT_DELAY_MS = 1000;

    /**
     * Adds an observer listener.
     */
    public void addListener(WebSocketListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes an observer listener.
     */
    public void removeListener(WebSocketListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Gets the current connection state.
     */
    public ConnectionState getConnectionState() {
        return connectionState.get();
    }

    /**
     * Checks if currently connected to the server.
     */
    public boolean isConnected() {
        return connectionState.get() == ConnectionState.CONNECTED;
    }

    /**
     * Connects to the specified WebSocket URL.
     */
    public CompletableFuture<WebSocketSession> connect(String url) {
        this.currentUrl = url;
        this.autoReconnect = true;

        if (!connectionState.compareAndSet(ConnectionState.DISCONNECTED, ConnectionState.CONNECTING)
                && !connectionState.compareAndSet(ConnectionState.RECONNECTING, ConnectionState.CONNECTING)) {
            log.info("WebSocket connection is already active or in progress. State: {}", connectionState.get());
            CompletableFuture<WebSocketSession> future = new CompletableFuture<>();
            if (connectionState.get() == ConnectionState.CONNECTED && session != null) {
                future.complete(session);
            } else {
                future.completeExceptionally(
                        new IllegalStateException("WebSocket is in state: " + connectionState.get()));
            }
            return future;
        }

        log.info("Connecting to WebSocket URL: {}", url);
        CompletableFuture<WebSocketSession> future = new CompletableFuture<>();
        try {
            webSocketClient.execute(this, url).whenComplete((s, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to establish WebSocket connection to: {}", url, throwable);
                    connectionState.set(ConnectionState.DISCONNECTED);
                    future.completeExceptionally(throwable);
                    triggerReconnect();
                } else {
                    this.session = s;
                    connectionState.set(ConnectionState.CONNECTED);
                    reconnectAttempts.set(0);
                    future.complete(s);
                }
            });
        } catch (Exception e) {
            log.error("Failed to invoke execute on WebSocketClient for URL: {}", url, e);
            connectionState.set(ConnectionState.DISCONNECTED);
            future.completeExceptionally(e);
            triggerReconnect();
        }
        return future;
    }

    /**
     * Gracefully disconnects the WebSocket session and disables auto-reconnect.
     */
    public void disconnect() {
        autoReconnect = false;
        connectionState.set(ConnectionState.DISCONNECTED);
        if (session != null && session.isOpen()) {
            try {
                log.info("Disconnecting WebSocket session: {}", session.getId());
                session.close();
            } catch (Exception e) {
                log.error("Error closing WebSocket session", e);
            }
        }
    }

    /**
     * Sends a WebSocketRequest object serialized as JSON.
     */
    public void send(T request) {
        if (!isConnected()) {
            log.warn("Cannot send message, WebSocket is not connected. Current state: {}", connectionState.get());
            throw new IllegalStateException("WebSocket is not connected");
        }
        try {
            String json = objectMapper.writeValueAsString(request);
            log.debug("Sending message: {}", json);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.error("Error sending WebSocket message: {}", request, e);
            onError(e);
        }
    }

    /**
     * Sends raw text message.
     */
    public void sendText(String text) {
        if (!isConnected()) {
            log.warn("Cannot send text, WebSocket is not connected. Current state: {}", connectionState.get());
            throw new IllegalStateException("WebSocket is not connected");
        }
        try {
            log.debug("Sending raw text: {}", text);
            session.sendMessage(new TextMessage(text));
        } catch (Exception e) {
            log.error("Error sending WebSocket text: {}", text, e);
            onError(e);
        }
    }

    @Override
    public final void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        connectionState.set(ConnectionState.CONNECTED);
        reconnectAttempts.set(0);
        log.info("WebSocket connection established. Session ID: {}", session.getId());
        onOpen(session);
        listeners.forEach(listener -> {
            try {
                listener.onOpen(session);
            } catch (Exception e) {
                log.error("Error invoking onOpen listener", e);
            }
        });
    }

    @Override
    protected final void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.trace("Received message: {}", message.getPayload());
        onMessage(message.getPayload());
        listeners.forEach(listener -> {
            try {
                listener.onMessage(message.getPayload());
            } catch (Exception e) {
                log.error("Error invoking onMessage listener", e);
            }
        });
    }

    @Override
    protected final void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.trace("Received binary message: {} bytes", message.getPayloadLength());
        onBinaryMessage(message.getPayload());
        listeners.forEach(listener -> {
            try {
                listener.onBinaryMessage(message.getPayload());
            } catch (Exception e) {
                log.error("Error invoking onBinaryMessage listener", e);
            }
        });
    }

    @Override
    public final void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session: {}", session.getId(), exception);
        onError(exception);
        listeners.forEach(listener -> {
            try {
                listener.onError(exception);
            } catch (Exception e) {
                log.error("Error invoking onError listener", e);
            }
        });
    }

    @Override
    public final void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed for session: {}. Status: {}", session.getId(), status);
        this.session = null;
        connectionState.set(ConnectionState.DISCONNECTED);
        onClose(status);
        listeners.forEach(listener -> {
            try {
                listener.onClose(status);
            } catch (Exception e) {
                log.error("Error invoking onClose listener", e);
            }
        });

        // Trigger reconnection if it was an unexpected disconnect
        if (autoReconnect) {
            triggerReconnect();
        }
    }

    private void triggerReconnect() {
        if (!autoReconnect) {
            return;
        }

        if (connectionState.compareAndSet(ConnectionState.DISCONNECTED, ConnectionState.RECONNECTING)
                || connectionState.get() == ConnectionState.RECONNECTING) {

            int attempts = reconnectAttempts.incrementAndGet();
            if (attempts > MAX_RECONNECT_ATTEMPTS) {
                log.error("Max reconnect attempts ({}) reached. Reconnection aborted.", MAX_RECONNECT_ATTEMPTS);
                connectionState.set(ConnectionState.DISCONNECTED);
                return;
            }

            long delay = INITIAL_RECONNECT_DELAY_MS * (long) Math.pow(2, attempts - 1);
            log.info("Attempting reconnection (attempt {}/{}) in {} ms...", attempts, MAX_RECONNECT_ATTEMPTS, delay);

            scheduler.schedule(
                    () -> {
                        try {
                            connect(currentUrl).whenComplete((s, ex) -> {
                                if (ex != null) {
                                    log.warn("Reconnection attempt {} failed. Retrying...", attempts);
                                    triggerReconnect();
                                } else {
                                    log.info("Reconnected successfully on attempt {}", attempts);
                                    reconnectAttempts.set(0);
                                }
                            });
                        } catch (Exception e) {
                            log.error("Error during reconnection attempt", e);
                            triggerReconnect();
                        }
                    },
                    delay,
                    TimeUnit.MILLISECONDS);
        }
    }

    protected abstract void onOpen(WebSocketSession session);

    protected abstract void onMessage(String message);

    protected abstract void onBinaryMessage(ByteBuffer message);

    protected abstract void onClose(CloseStatus status);

    protected abstract void onError(Throwable exception);
}
