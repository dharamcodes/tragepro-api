package com.tragepro.api.feed.client;

import com.tragepro.api.marketdata.feed.auth.AuthenticationStrategy;
import com.tragepro.api.marketdata.feed.client.AbstractFeedWebSocketClient;
import com.tragepro.api.marketdata.feed.listener.FeedMessageListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AbstractFeedWebSocketClientTest {

    static class DummyWebSocketClient extends AbstractFeedWebSocketClient {
        boolean enrichCalled = false;
        boolean onConnectCalled = false;
        boolean onDisconnectCalled = false;
        String lastMessage = null;

        public DummyWebSocketClient(WebSocketClient webSocketClient, AuthenticationStrategy authenticationStrategy, String webSocketUrl) {
            super(webSocketClient, authenticationStrategy, webSocketUrl);
        }

        @Override
        protected void enrichHeaders(HttpHeaders headers) {
            enrichCalled = true;
        }

        @Override
        protected URI buildUri(String baseUrl) {
            return URI.create(baseUrl);
        }

        @Override
        protected void onConnect() {
            onConnectCalled = true;
        }

        @Override
        protected void onDisconnect() {
            onDisconnectCalled = true;
        }

        @Override
        protected void handleRawMessage(String message) {
            lastMessage = message;
        }
        
        // Making accessible for test
        public void publicHandleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            super.handleTextMessage(session, message);
        }
    }

    @Test
    void testConnectAndLifecycle() throws Exception {
        WebSocketClient mockWsClient = Mockito.mock(WebSocketClient.class);
        AuthenticationStrategy mockStrategy = Mockito.mock(AuthenticationStrategy.class);
        WebSocketSession mockSession = Mockito.mock(WebSocketSession.class);

        when(mockWsClient.execute(any(), any(WebSocketHttpHeaders.class), any(URI.class)))
                .thenReturn(CompletableFuture.completedFuture(mockSession));

        DummyWebSocketClient client = new DummyWebSocketClient(mockWsClient, mockStrategy, "ws://localhost");
        
        FeedMessageListener listener = Mockito.mock(FeedMessageListener.class);
        client.addListener(listener);
        
        client.connect();

        verify(mockStrategy).applyAuthentication(any());
        assertTrue(client.enrichCalled);
        
        client.afterConnectionEstablished(mockSession);
        assertTrue(client.onConnectCalled);
        verify(listener).onConnected();

        client.publicHandleTextMessage(mockSession, new TextMessage("hello"));
        verify(listener).onMessageReceived("hello");

        client.afterConnectionClosed(mockSession, CloseStatus.NORMAL);
        assertTrue(client.onDisconnectCalled);
        verify(listener).onDisconnected();
    }
}
