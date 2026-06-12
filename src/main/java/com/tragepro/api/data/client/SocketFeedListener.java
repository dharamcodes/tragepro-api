package com.tragepro.api.data.client;

import java.nio.ByteBuffer;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public interface SocketFeedListener {
    default void onOpen(WebSocketSession session) {}

    default void onTextMessage(String message) {}

    default void onBinaryMessage(ByteBuffer message) {}

    default void onClose(CloseStatus status) {}

    default void onError(Throwable exception) {}
}
