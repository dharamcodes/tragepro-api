package com.tragepro.api.candle.provider.websocket;

import static org.mockito.Mockito.*;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

class BrokerWebSocketClientTest {

    private ApplicationEventPublisher eventPublisher;
    private BrokerMessageHandler messageHandler;
    private BrokerWebSocketClient client;
    private WebSocketSession session;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(ApplicationEventPublisher.class);
        messageHandler = mock(BrokerMessageHandler.class);
        when(messageHandler.supports("generic")).thenReturn(true);
        when(messageHandler.supports("other")).thenReturn(false);

        client = new BrokerWebSocketClient("generic", List.of(messageHandler), eventPublisher);
        session = mock(WebSocketSession.class);
    }

    @Test
    void testAfterConnectionEstablished() throws Exception {
        // Just verify it doesn't throw exceptions
        client.afterConnectionEstablished(session);
        verify(session, never()).close();
    }

    @Test
    void testHandleTextMessage_Handled() throws Exception {
        String payload = "data";
        CandleReceivedEvent event =
                new CandleReceivedEvent(new Symbol("S", "Name"), new Candle(1L, 100.0, 100.0, 100.0, 100.0, 100.0));
        when(messageHandler.handleMessage(session, payload)).thenReturn(event);

        client.handleTextMessage(session, new TextMessage(payload));

        verify(eventPublisher, times(1)).publishEvent(event);
    }

    @Test
    void testHandleTextMessage_NotHandled() throws Exception {
        String payload = "data";
        when(messageHandler.handleMessage(session, payload)).thenReturn(null);

        client.handleTextMessage(session, new TextMessage(payload));

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testHandleTextMessage_NoSupportedHandler() throws Exception {
        BrokerWebSocketClient otherClient = new BrokerWebSocketClient("other", List.of(messageHandler), eventPublisher);
        String payload = "data";

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            otherClient.handleTextMessage(session, new TextMessage(payload));
        });

        verify(messageHandler, never()).handleMessage(any(), any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testAfterConnectionClosed() throws Exception {
        client.afterConnectionClosed(session, org.springframework.web.socket.CloseStatus.NORMAL);
        verify(session, never()).close();
    }
}
