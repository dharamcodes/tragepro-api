package com.tragepro.api.data.client.socket.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.data.client.socket.WebSocketListener;
import com.tragepro.api.data.client.socket.config.DhanFeedProperties;
import com.tragepro.api.data.client.socket.constant.ConnectionState;
import com.tragepro.api.data.model.request.DhanWebSocketRequest;
import com.tragepro.api.data.model.response.dhan.DhanFullPacket;
import com.tragepro.api.data.model.response.dhan.DhanOiPacket;
import com.tragepro.api.data.model.response.dhan.DhanPrevClosePacket;
import com.tragepro.api.data.model.response.dhan.DhanQuotePacket;
import com.tragepro.api.data.model.response.dhan.DhanTickerPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

@ExtendWith(MockitoExtension.class)
class DhanWebSocketImplTest {

    @Mock
    private WebSocketClient webSocketClient;

    @Mock
    private WebSocketSession webSocketSession;

    @Mock
    private WebSocketListener webSocketListener;

    private ObjectMapper objectMapper;
    private DhanFeedProperties properties;
    private WebSocketImpl dhanWebSocket;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        properties = new DhanFeedProperties();
        properties.setClientId("test-client-id");
        properties.setAccessToken("test-token");
        properties.setWebsocketUrl("wss://api-feed.dhan.co");

        dhanWebSocket = new WebSocketImpl(webSocketClient, objectMapper, properties);
        dhanWebSocket.addListener(webSocketListener);
    }

    @Test
    void testInitialState() {
        assertEquals(ConnectionState.DISCONNECTED, dhanWebSocket.getConnectionState());
        assertFalse(dhanWebSocket.isConnected());
    }

    @Test
    void testConnectSuccess() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(eq(dhanWebSocket), eq("wss://api-feed.dhan.co")))
                .thenReturn(clientFuture);

        CompletableFuture<WebSocketSession> connectFuture = dhanWebSocket.connect("wss://api-feed.dhan.co");
        WebSocketSession session = connectFuture.get();

        assertNotNull(session);
        assertEquals(webSocketSession, session);
        assertEquals(ConnectionState.CONNECTED, dhanWebSocket.getConnectionState());
        assertTrue(dhanWebSocket.isConnected());

        dhanWebSocket.afterConnectionEstablished(webSocketSession);
        verify(webSocketListener).onOpen(webSocketSession);
    }

    @Test
    void testConnectWithProperties() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = CompletableFuture.completedFuture(webSocketSession);
        String expectedUrl = "wss://api-feed.dhan.co?version=2&token=test-token&clientId=test-client-id&authType=2";
        when(webSocketClient.execute(eq(dhanWebSocket), eq(expectedUrl))).thenReturn(clientFuture);

        CompletableFuture<WebSocketSession> connectFuture = dhanWebSocket.connect();
        WebSocketSession session = connectFuture.get();

        assertNotNull(session);
        assertEquals(webSocketSession, session);
        assertEquals(ConnectionState.CONNECTED, dhanWebSocket.getConnectionState());
    }

    @Test
    void testConnectFailure() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = new CompletableFuture<>();
        clientFuture.completeExceptionally(new RuntimeException("Connection failed"));
        when(webSocketClient.execute(eq(dhanWebSocket), eq("wss://api-feed.dhan.co")))
                .thenReturn(clientFuture);

        CompletableFuture<WebSocketSession> connectFuture = dhanWebSocket.connect("wss://api-feed.dhan.co");

        assertThrows(Exception.class, connectFuture::get);
        assertEquals(ConnectionState.RECONNECTING, dhanWebSocket.getConnectionState());
        assertFalse(dhanWebSocket.isConnected());
    }

    @Test
    void testSendThrowsWhenDisconnected() {
        DhanWebSocketRequest request = new DhanWebSocketRequest();
        request.setId("1");
        request.setAction("subscribe");

        assertThrows(IllegalStateException.class, () -> dhanWebSocket.send(request));
        assertThrows(IllegalStateException.class, () -> dhanWebSocket.sendText("hello"));
    }

    @Test
    void testSendSuccess() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(eq(dhanWebSocket), eq("wss://api-feed.dhan.co")))
                .thenReturn(clientFuture);
        dhanWebSocket.connect("wss://api-feed.dhan.co").get();

        dhanWebSocket.afterConnectionEstablished(webSocketSession);

        DhanWebSocketRequest request = new DhanWebSocketRequest();
        request.setId("1");
        request.setAction("subscribe");

        dhanWebSocket.send(request);

        ArgumentCaptor<TextMessage> textMessageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(textMessageCaptor.capture());

        String payload = textMessageCaptor.getValue().getPayload();
        assertTrue(payload.contains("\"id\":\"1\""));
        assertTrue(payload.contains("\"action\":\"subscribe\""));
    }

    @Test
    void testSendTextSuccess() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(eq(dhanWebSocket), eq("wss://api-feed.dhan.co")))
                .thenReturn(clientFuture);
        dhanWebSocket.connect("wss://api-feed.dhan.co").get();

        dhanWebSocket.afterConnectionEstablished(webSocketSession);

        dhanWebSocket.sendText("raw-message");

        ArgumentCaptor<TextMessage> textMessageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(textMessageCaptor.capture());
        assertEquals("raw-message", textMessageCaptor.getValue().getPayload());
    }

    @Test
    void testMessageCallbacks() throws Exception {
        dhanWebSocket.afterConnectionEstablished(webSocketSession);

        // Test text message callback
        TextMessage textMessage = new TextMessage("hello text");
        dhanWebSocket.handleMessage(webSocketSession, textMessage);
        verify(webSocketListener).onMessage("hello text");

        // Test general binary message callback
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] {1, 2, 3});
        BinaryMessage binaryMessage = new BinaryMessage(byteBuffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);
        verify(webSocketListener).onBinaryMessage(byteBuffer);

        // Test error callback
        Throwable exception = new RuntimeException("Transport error");
        dhanWebSocket.handleTransportError(webSocketSession, exception);
        verify(webSocketListener).onError(exception);

        // Test close callback
        CloseStatus status = CloseStatus.NORMAL;
        dhanWebSocket.afterConnectionClosed(webSocketSession, status);
        verify(webSocketListener).onClose(status);
    }

    private ByteBuffer createHeaderBuffer(
            byte responseCode, short messageLength, byte exchangeSegment, int securityId, int totalSize) {
        ByteBuffer buffer = ByteBuffer.allocate(totalSize).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(responseCode);
        buffer.putShort(messageLength);
        buffer.put(exchangeSegment);
        buffer.putInt(securityId);
        return buffer;
    }

    @Test
    void testParseTickerPacket() throws Exception {
        // Header (8) + LTP (4) + LTT (4) = 16 bytes
        ByteBuffer buffer = createHeaderBuffer((byte) 2, (short) 16, (byte) 1, 1333, 16);
        buffer.putFloat(123.45f); // lastTradedPrice
        buffer.putInt(1600000000); // lastTradeTime
        buffer.flip();

        BinaryMessage binaryMessage = new BinaryMessage(buffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);

        ArgumentCaptor<DhanTickerPacket> captor = ArgumentCaptor.forClass(DhanTickerPacket.class);
        verify(webSocketListener).onDhanTicker(captor.capture());
        DhanTickerPacket packet = captor.getValue();
        assertEquals(2, packet.getHeader().getResponseCode());
        assertEquals(16, packet.getHeader().getMessageLength());
        assertEquals(1, packet.getHeader().getExchangeSegment());
        assertEquals(1333, packet.getHeader().getSecurityId());
        assertEquals(123.45f, packet.getLastTradedPrice(), 0.001f);
        assertEquals(1600000000, packet.getLastTradeTime());
    }

    @Test
    void testParseQuotePacket() throws Exception {
        // Header (8) + LTP (4) + LTQ (2) + LTT (4) + ATP (4) + Vol (4) + SellQty (4) + BuyQty (4) + Open (4) + Close
        // (4)
        // + High (4) + Low (4) = 50 bytes total
        ByteBuffer buffer = createHeaderBuffer((byte) 4, (short) 50, (byte) 1, 1333, 50);
        buffer.putFloat(123.45f); // lastTradedPrice
        buffer.putShort((short) 100); // lastTradedQuantity
        buffer.putInt(1600000000); // lastTradeTime
        buffer.putFloat(122.50f); // averageTradePrice
        buffer.putInt(100000); // volume
        buffer.putInt(60000); // totalSellQuantity
        buffer.putInt(40000); // totalBuyQuantity
        buffer.putFloat(120.00f); // open
        buffer.putFloat(121.00f); // close
        buffer.putFloat(125.00f); // high
        buffer.putFloat(119.00f); // low
        buffer.flip();

        BinaryMessage binaryMessage = new BinaryMessage(buffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);

        ArgumentCaptor<DhanQuotePacket> captor = ArgumentCaptor.forClass(DhanQuotePacket.class);
        verify(webSocketListener).onDhanQuote(captor.capture());
        DhanQuotePacket packet = captor.getValue();
        assertEquals(123.45f, packet.getLastTradedPrice(), 0.001f);
        assertEquals(100, packet.getLastTradedQuantity());
        assertEquals(1600000000, packet.getLastTradeTime());
        assertEquals(122.50f, packet.getAverageTradePrice(), 0.001f);
        assertEquals(100000, packet.getVolume());
        assertEquals(60000, packet.getTotalSellQuantity());
        assertEquals(40000, packet.getTotalBuyQuantity());
        assertEquals(120.00f, packet.getOpen(), 0.001f);
        assertEquals(121.00f, packet.getClose(), 0.001f);
        assertEquals(125.00f, packet.getHigh(), 0.001f);
        assertEquals(119.00f, packet.getLow(), 0.001f);
    }

    @Test
    void testParseOiPacket() throws Exception {
        // Header (8) + OI (4) = 12 bytes total
        ByteBuffer buffer = createHeaderBuffer((byte) 5, (short) 12, (byte) 2, 532540, 12);
        buffer.putInt(50000); // openInterest
        buffer.flip();

        BinaryMessage binaryMessage = new BinaryMessage(buffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);

        ArgumentCaptor<DhanOiPacket> captor = ArgumentCaptor.forClass(DhanOiPacket.class);
        verify(webSocketListener).onDhanOi(captor.capture());
        DhanOiPacket packet = captor.getValue();
        assertEquals(50000, packet.getOpenInterest());
    }

    @Test
    void testParsePrevClosePacket() throws Exception {
        // Header (8) + Price (4) + OI (4) = 16 bytes total
        ByteBuffer buffer = createHeaderBuffer((byte) 6, (short) 16, (byte) 2, 532540, 16);
        buffer.putFloat(120.50f); // prevClosePrice
        buffer.putInt(45000); // prevOpenInterest
        buffer.flip();

        BinaryMessage binaryMessage = new BinaryMessage(buffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);

        ArgumentCaptor<DhanPrevClosePacket> captor = ArgumentCaptor.forClass(DhanPrevClosePacket.class);
        verify(webSocketListener).onDhanPrevClose(captor.capture());
        DhanPrevClosePacket packet = captor.getValue();
        assertEquals(120.50f, packet.getPrevClosePrice(), 0.001f);
        assertEquals(45000, packet.getPrevOpenInterest());
    }

    @Test
    void testParseFullPacket() throws Exception {
        // Header (8) + Quote/OI Payload (54) + Depth (100) = 162 bytes total
        ByteBuffer buffer = createHeaderBuffer((byte) 8, (short) 162, (byte) 1, 1333, 162);
        buffer.putFloat(123.45f); // lastTradedPrice
        buffer.putShort((short) 100); // lastTradedQuantity
        buffer.putInt(1600000000); // lastTradeTime
        buffer.putFloat(122.50f); // averageTradePrice
        buffer.putInt(100000); // volume
        buffer.putInt(60000); // totalSellQuantity
        buffer.putInt(40000); // totalBuyQuantity
        buffer.putInt(50000); // openInterest
        buffer.putInt(55000); // highOpenInterest
        buffer.putInt(45000); // lowOpenInterest
        buffer.putFloat(120.00f); // open
        buffer.putFloat(121.00f); // close
        buffer.putFloat(125.00f); // high
        buffer.putFloat(119.00f); // low

        // Add 5 levels of market depth (20 bytes each: BidQty (4) + AskQty (4) + BidOrd (2) + AskOrd (2) + BidPrice (4)
        // + AskPrice (4))
        for (int i = 0; i < 5; i++) {
            buffer.putInt(1000 + i); // bidQuantity
            buffer.putInt(2000 + i); // askQuantity
            buffer.putShort((short) (10 + i)); // bidOrders
            buffer.putShort((short) (20 + i)); // askOrders
            buffer.putFloat(123.00f + i); // bidPrice
            buffer.putFloat(124.00f + i); // askPrice
        }
        buffer.flip();

        BinaryMessage binaryMessage = new BinaryMessage(buffer);
        dhanWebSocket.handleMessage(webSocketSession, binaryMessage);

        ArgumentCaptor<DhanFullPacket> captor = ArgumentCaptor.forClass(DhanFullPacket.class);
        verify(webSocketListener).onDhanFull(captor.capture());
        DhanFullPacket packet = captor.getValue();
        assertEquals(123.45f, packet.getLastTradedPrice(), 0.001f);
        assertEquals(50000, packet.getOpenInterest());
        assertEquals(5, packet.getMarketDepth().size());
        assertEquals(1000, packet.getMarketDepth().get(0).getBidQuantity());
        assertEquals(123.00f, packet.getMarketDepth().get(0).getBidPrice(), 0.001f);
    }

    @Test
    void testExplicitDisconnect() throws Exception {
        CompletableFuture<WebSocketSession> clientFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(eq(dhanWebSocket), eq("wss://api-feed.dhan.co")))
                .thenReturn(clientFuture);
        dhanWebSocket.connect("wss://api-feed.dhan.co").get();

        when(webSocketSession.isOpen()).thenReturn(true);
        dhanWebSocket.afterConnectionEstablished(webSocketSession);

        dhanWebSocket.disconnect();

        verify(webSocketSession).close();
        assertEquals(ConnectionState.DISCONNECTED, dhanWebSocket.getConnectionState());
        assertFalse(dhanWebSocket.isConnected());
    }

    @Test
    void testRemoveListener() throws Exception {
        dhanWebSocket.removeListener(webSocketListener);
        dhanWebSocket.afterConnectionEstablished(webSocketSession);
        verify(webSocketListener, never()).onOpen(any());
    }
}
