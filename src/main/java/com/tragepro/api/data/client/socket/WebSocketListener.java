package com.tragepro.api.data.client.socket;

import java.nio.ByteBuffer;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/**
 * Listener interface for observing WebSocket events (Observer Pattern).
 */
public interface WebSocketListener {
    /**
     * Invoked when the WebSocket connection is successfully established.
     *
     * @param session the established WebSocket session
     */
    void onOpen(WebSocketSession session);

    /**
     * Invoked when a text message is received from the WebSocket.
     *
     * @param message the text payload
     */
    void onMessage(String message);

    /**
     * Invoked when a binary message is received from the WebSocket.
     *
     * @param message the binary payload
     */
    void onBinaryMessage(ByteBuffer message);

    /**
     * Invoked when the WebSocket connection is closed.
     *
     * @param status the close status
     */
    void onClose(CloseStatus status);

    /**
     * Invoked when a transport or system error occurs.
     *
     * @param exception the error details
     */
    void onError(Throwable exception);

    /**
     * Invoked when a Dhan Ticker packet is parsed.
     *
     * @param ticker the DhanTickerPacket data
     */
    default void onDhanTicker(com.tragepro.api.data.model.response.dhan.DhanTickerPacket ticker) {}

    /**
     * Invoked when a Dhan Quote packet is parsed.
     *
     * @param quote the DhanQuotePacket data
     */
    default void onDhanQuote(com.tragepro.api.data.model.response.dhan.DhanQuotePacket quote) {}

    /**
     * Invoked when a Dhan Open Interest (OI) packet is parsed.
     *
     * @param oi the DhanOiPacket data
     */
    default void onDhanOi(com.tragepro.api.data.model.response.dhan.DhanOiPacket oi) {}

    /**
     * Invoked when a Dhan Previous Close packet is parsed.
     *
     * @param prevClose the DhanPrevClosePacket data
     */
    default void onDhanPrevClose(com.tragepro.api.data.model.response.dhan.DhanPrevClosePacket prevClose) {}

    /**
     * Invoked when a Dhan Full packet (Quote, OI and Depth) is parsed.
     *
     * @param full the DhanFullPacket data
     */
    default void onDhanFull(com.tragepro.api.data.model.response.dhan.DhanFullPacket full) {}
}
