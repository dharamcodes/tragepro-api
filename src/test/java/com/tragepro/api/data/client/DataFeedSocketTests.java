package com.tragepro.api.data.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.data.client.helper.DataSocketHelper;
import com.tragepro.api.data.model.SocketConfig;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
class DataFeedSocketTests {

    @Mock
    private WebSocketClient webSocketClient;

    @Mock
    private WebSocketSession webSocketSession;

    @Mock
    private ScheduledExecutorService mockScheduler;

    private SocketConfig properties;
    private DataSocketHelper helper;
    private DataFeedSocket socket;

    @BeforeEach
    void setUp() throws Exception {
        properties = new SocketConfig();
        properties.setUrl("wss://test-feed.example.com");
        properties.setToken("testToken");
        properties.setClientId("testClient");
        properties.setReconnect(true);
        properties.setUrlPattern("%s?token=%s&clientId=%s");
        properties.setMaxReconnectAttempts(5);
        properties.setInitialReconnectDelay(1000);
        properties.setMaxReconnectDelay(30000);

        helper = new DataSocketHelper(mockScheduler, properties);
        socket = new DataFeedSocket(webSocketClient, properties, helper);
    }

    @Test
    void testHelperBuildConnectionUrl() {
        assertEquals(
                "wss://test-feed.example.com?token=testToken&clientId=testClient",
                helper.buildConnectionUrl(properties));

        // Test with null pattern throws IllegalStateException
        properties.setUrlPattern(null);
        assertThrows(IllegalStateException.class, () -> helper.buildConnectionUrl(properties));

        // Test with null values throws IllegalStateException
        properties.setUrlPattern("%s?token=%s&clientId=%s");
        properties.setUrl(null);
        assertThrows(IllegalStateException.class, () -> helper.buildConnectionUrl(properties));

        // Test IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> helper.buildConnectionUrl(null));
    }

    @Test
    void testHelperCalculateBackoffDelay() {
        assertEquals(0, helper.calculateBackoffDelay(0));
        assertEquals(0, helper.calculateBackoffDelay(-1));
        assertEquals(1000, helper.calculateBackoffDelay(1));
        assertEquals(2000, helper.calculateBackoffDelay(2));
        assertEquals(4000, helper.calculateBackoffDelay(3));
        assertEquals(30000, helper.calculateBackoffDelay(10)); // max limit check
    }

    @Test
    void testHelperShutdownSuccess() throws Exception {
        when(mockScheduler.awaitTermination(anyLong(), any())).thenReturn(true);
        helper.shutdown();
        verify(mockScheduler, times(1)).shutdown();
    }

    @Test
    void testHelperShutdownTimeout() throws Exception {
        when(mockScheduler.awaitTermination(anyLong(), any())).thenReturn(false);
        helper.shutdown();
        verify(mockScheduler, times(1)).shutdownNow();
    }

    @Test
    void testHelperShutdownInterruptedException() throws Exception {
        when(mockScheduler.awaitTermination(anyLong(), any())).thenThrow(new InterruptedException());
        helper.shutdown();
        verify(mockScheduler, times(1)).shutdownNow();
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    void testConnectSuccess() {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);

        CompletableFuture<WebSocketSession> result = socket.connect();

        assertNotNull(result);
        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        assertEquals(webSocketSession, result.join());
        assertFalse(socket.isConnected()); // Mock session has isOpen default as false
    }

    @Test
    void testConnectSuccessWithOpenSession() {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);

        CompletableFuture<WebSocketSession> result = socket.connect();
        assertEquals(webSocketSession, result.join());
        assertTrue(socket.isConnected());

        // Subsequent connect should return the existing open session directly
        CompletableFuture<WebSocketSession> result2 = socket.connect();
        assertTrue(result2.isDone());
        assertEquals(webSocketSession, result2.join());
    }

    @Test
    void testConnectWhileConnectingThrows() throws Exception {
        CompletableFuture<WebSocketSession> connectionFuture = new CompletableFuture<>();
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);

        CompletableFuture<WebSocketSession> result1 = socket.connect();
        assertFalse(result1.isDone());

        // Subsequent connect while connecting should fail
        CompletableFuture<WebSocketSession> result2 = socket.connect();
        assertTrue(result2.isDone());
        assertTrue(result2.isCompletedExceptionally());
    }

    @Test
    void testConnectExecuteThrowsException() {
        when(webSocketClient.execute(any(), anyString())).thenThrow(new RuntimeException("Connection error"));

        CompletableFuture<WebSocketSession> result = socket.connect();

        assertTrue(result.isDone());
        assertTrue(result.isCompletedExceptionally());
        verify(mockScheduler, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testConnectFutureCompletesExceptionally() {
        CompletableFuture<WebSocketSession> connectionFuture = new CompletableFuture<>();
        connectionFuture.completeExceptionally(new RuntimeException("Connection error"));
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);

        CompletableFuture<WebSocketSession> result = socket.connect();

        assertTrue(result.isDone());
        assertTrue(result.isCompletedExceptionally());
        verify(mockScheduler, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testDisconnect() throws IOException {
        // Disconnect with null session
        socket.disconnect(); // should not throw

        // Disconnect with open session
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);

        socket.connect();
        socket.disconnect();
        verify(webSocketSession, times(1)).close();
    }

    @Test
    void testDisconnectThrowsIOException() throws IOException {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);
        doThrow(new IOException("Close failure")).when(webSocketSession).close();

        socket.connect();
        socket.disconnect(); // should catch exception and not throw
        verify(webSocketSession, times(1)).close();
    }

    @Test
    void testSendTextSuccess() throws IOException {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);

        socket.connect();
        socket.sendText("hello");

        verify(webSocketSession, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void testSendTextNotConnectedThrows() {
        assertThrows(IllegalStateException.class, () -> socket.sendText("hello"));
    }

    @Test
    void testSendTextThrowsIOException() throws IOException {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);
        doThrow(new IOException("Send failure")).when(webSocketSession).sendMessage(any(TextMessage.class));

        SocketFeedListener listener = mock(SocketFeedListener.class);
        socket.addListener(listener);

        socket.connect();
        socket.sendText("hello");

        verify(listener, times(1)).onError(any(IOException.class));
    }

    @Test
    void testSendBinarySuccess() throws IOException {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);

        socket.connect();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[] {1, 2, 3});
        socket.sendBinary(buffer);

        verify(webSocketSession, times(1)).sendMessage(any(BinaryMessage.class));
    }

    @Test
    void testSendBinaryNotConnectedThrows() {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[] {1, 2, 3});
        assertThrows(IllegalStateException.class, () -> socket.sendBinary(buffer));
    }

    @Test
    void testSendBinaryThrowsIOException() throws IOException {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        when(webSocketSession.isOpen()).thenReturn(true);
        doThrow(new IOException("Send binary failure")).when(webSocketSession).sendMessage(any(BinaryMessage.class));

        SocketFeedListener listener = mock(SocketFeedListener.class);
        socket.addListener(listener);

        socket.connect();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[] {1, 2, 3});
        socket.sendBinary(buffer);

        verify(listener, times(1)).onError(any(IOException.class));
    }

    @Test
    void testListenersRegistrationAndCallbacks() throws Exception {
        SocketFeedListener listener = mock(SocketFeedListener.class);
        socket.addListener(listener);

        // test open
        socket.afterConnectionEstablished(webSocketSession);
        verify(listener, times(1)).onOpen(webSocketSession);

        // test text message
        TextMessage textMessage = new TextMessage("hello");
        socket.handleTextMessage(webSocketSession, textMessage);
        verify(listener, times(1)).onTextMessage("hello");

        // test binary message
        ByteBuffer payload = ByteBuffer.wrap(new byte[] {5, 6});
        BinaryMessage binaryMessage = new BinaryMessage(payload);
        socket.handleBinaryMessage(webSocketSession, binaryMessage);
        verify(listener, times(1)).onBinaryMessage(any(ByteBuffer.class));

        // test error
        RuntimeException exception = new RuntimeException("Test error");
        socket.handleTransportError(webSocketSession, exception);
        verify(listener, times(1)).onError(exception);

        // test close
        CloseStatus status = CloseStatus.NORMAL;
        socket.afterConnectionClosed(webSocketSession, status);
        verify(listener, times(1)).onClose(status);

        // remove listener and verify no further callbacks
        socket.removeListener(listener);
        socket.afterConnectionEstablished(webSocketSession);
        verify(listener, times(1)).onOpen(webSocketSession); // still 1
    }

    @Test
    void testListenerExceptionsDoNotCrashHandler() throws Exception {
        SocketFeedListener listener = mock(SocketFeedListener.class);
        doThrow(new RuntimeException("listener open fail")).when(listener).onOpen(any());
        doThrow(new RuntimeException("listener text fail")).when(listener).onTextMessage(anyString());
        doThrow(new RuntimeException("listener binary fail")).when(listener).onBinaryMessage(any());
        doThrow(new RuntimeException("listener close fail")).when(listener).onClose(any());
        doThrow(new RuntimeException("listener error fail")).when(listener).onError(any());

        socket.addListener(listener);

        assertDoesNotThrow(() -> socket.afterConnectionEstablished(webSocketSession));
        assertDoesNotThrow(() -> socket.handleTextMessage(webSocketSession, new TextMessage("test")));
        assertDoesNotThrow(() -> socket.handleBinaryMessage(webSocketSession, new BinaryMessage(new byte[0])));
        assertDoesNotThrow(() -> socket.handleTransportError(webSocketSession, new RuntimeException()));
        assertDoesNotThrow(() -> socket.afterConnectionClosed(webSocketSession, CloseStatus.NORMAL));
    }

    @Test
    void testTriggerReconnectMaximumAttempts() {
        properties.setReconnect(true);
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        socket.connect();

        // Trigger first 5 reconnects
        for (int i = 0; i < 5; i++) {
            socket.afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);
        }
        verify(mockScheduler, times(5)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));

        // 6th closed event should not schedule any reconnect
        socket.afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);
        verify(mockScheduler, times(5)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testReconnectionExecution() {
        CompletableFuture<WebSocketSession> connectionFuture = CompletableFuture.completedFuture(webSocketSession);
        when(webSocketClient.execute(any(), anyString())).thenReturn(connectionFuture);
        socket.connect();

        socket.afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(mockScheduler, times(1)).schedule(runnableCaptor.capture(), anyLong(), eq(TimeUnit.MILLISECONDS));

        // Execute the captured reconnect runnable
        runnableCaptor.getValue().run();

        // Reconnect succeeds, which should reset attempts. Let's verify by triggering again
        socket.afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);
        verify(mockScheduler, times(2)).schedule(any(Runnable.class), anyLong(), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void testHelperDefaultConstructor() {
        DataSocketHelper defaultHelper = new DataSocketHelper(properties);
        assertNotNull(defaultHelper);
        defaultHelper.shutdown();
    }
}
