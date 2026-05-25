package com.tragepro.api.candle.provider.websocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.candle.event.CandleReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

class GenericBrokerMessageHandlerTest {

    private GenericBrokerMessageHandler handler;
    private WebSocketSession session;

    @BeforeEach
    void setUp() {
        handler = new GenericBrokerMessageHandler();
        session = mock(WebSocketSession.class);
    }

    @Test
    void testSupports() {
        assertTrue(handler.supports("generic"));
        assertFalse(handler.supports("other"));
    }

    @Test
    void testHandleMessage_Success() {
        String json =
                "{\"symbol\":\"BTCUSD\",\"name\":\"Bitcoin\",\"timestamp\":1700000000,\"open\":100.5,\"high\":110.0,\"low\":90.0,\"close\":105.0,\"volume\":50.0}";

        CandleReceivedEvent event = handler.handleMessage(session, json);

        assertNotNull(event);
        assertEquals("BTCUSD", event.symbol().id());
        assertEquals("Bitcoin", event.symbol().name());
        assertEquals(100.5, event.candle().open());
        assertEquals(110.0, event.candle().high());
        assertEquals(90.0, event.candle().low());
        assertEquals(105.0, event.candle().close());
        assertEquals(50.0, event.candle().volume());
        assertEquals(1700000000L, event.candle().timestamp());
    }

    @Test
    void testHandleMessage_InvalidJson() {
        String json = "invalid json";
        CandleReceivedEvent event = handler.handleMessage(session, json);
        assertNull(event);
    }
}
