package com.tragepro.api.data.client.helper;

import com.tragepro.api.data.client.DataFeedSocket;
import com.tragepro.api.data.client.SocketFeedListener;
import com.tragepro.api.data.model.SocketConfig;
import jakarta.annotation.PreDestroy;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class DataSocketHelper {

    private final ScheduledExecutorService scheduler;
    private final SocketConfig socketConfig;
    private final List<SocketFeedListener> listeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean autoReconnect = new AtomicBoolean(false);
    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);

    @Autowired
    public DataSocketHelper(SocketConfig socketConfig) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.socketConfig = socketConfig;
    }

    public DataSocketHelper(ScheduledExecutorService scheduler, SocketConfig socketConfig) {
        this.scheduler = scheduler;
        this.socketConfig = socketConfig;
    }

    public void addListener(SocketFeedListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(SocketFeedListener listener) {
        listeners.remove(listener);
    }

    private void dispatchSafely(java.util.function.Consumer<SocketFeedListener> action) {
        listeners.forEach(listener -> {
            try {
                action.accept(listener);
            } catch (Exception e) {
                log.error("Error invoking listener callback", e);
            }
        });
    }

    public void dispatchOpen(WebSocketSession session) {
        dispatchSafely(l -> l.onOpen(session));
    }

    public void dispatchTextMessage(String message) {
        dispatchSafely(l -> l.onTextMessage(message));
    }

    public void dispatchBinaryMessage(ByteBuffer message) {
        dispatchSafely(l -> l.onBinaryMessage(message.duplicate()));
    }

    public void dispatchError(Throwable exception) {
        dispatchSafely(l -> l.onError(exception));
    }

    public void dispatchClose(CloseStatus status) {
        dispatchSafely(l -> l.onClose(status));
    }

    public boolean isAutoReconnect() {
        return autoReconnect.get();
    }

    public void setAutoReconnect(boolean value) {
        autoReconnect.set(value);
    }

    public void resetReconnectAttempts() {
        reconnectAttempts.set(0);
    }

    public void triggerReconnect(DataFeedSocket socket, String currentUrl) {
        if (!autoReconnect.get()) {
            return;
        }

        int attempt = reconnectAttempts.incrementAndGet();
        int maxAttempts = socketConfig.getMaxReconnectAttempts();
        if (attempt > maxAttempts) {
            log.error("Max reconnection attempts reached. Giving up.");
            return;
        }

        long delay = calculateBackoffDelay(attempt);
        if (delay > 0) {
            // Add random jitter of ±15%
            double jitter = (Math.random() - 0.5) * 2 * 0.15 * delay;
            delay = Math.max(0, delay + (long) jitter);
        }
        log.info("Reconnecting in {} ms (attempt {}/{})", delay, attempt, maxAttempts);

        scheduler.schedule(
                () -> {
                    if (autoReconnect.get()) {
                        socket.connect(currentUrl).thenRun(() -> log.info("Reconnected successfully."));
                    }
                },
                delay,
                TimeUnit.MILLISECONDS);
    }

    public String buildConnectionUrl(SocketConfig properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties cannot be null");
        }
        String pattern = properties.getUrlPattern();
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalStateException("urlPattern must be configured in application yml");
        }
        String url = properties.getUrl();
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("url must be configured in application yml");
        }
        String token = properties.getToken();
        if (token == null) {
            token = "";
        }
        String clientId = properties.getClientId();
        if (clientId == null) {
            clientId = "";
        }
        return String.format(pattern, url, token, clientId);
    }

    public long calculateBackoffDelay(int attempt) {
        if (attempt <= 0) {
            return 0;
        }
        long initialDelay = socketConfig.getInitialReconnectDelay();
        long maxDelay = socketConfig.getMaxReconnectDelay();
        return (long) Math.min(maxDelay, initialDelay * Math.pow(2, attempt - 1));
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
