package com.tragepro.api.data.client.socket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.data.client.socket.AbstractWebSocket;
import com.tragepro.api.data.client.socket.config.DhanFeedProperties;
import com.tragepro.api.data.model.request.DhanWebSocketRequest;
import com.tragepro.api.data.model.response.dhan.DhanDepthLevel;
import com.tragepro.api.data.model.response.dhan.DhanFullPacket;
import com.tragepro.api.data.model.response.dhan.DhanHeader;
import com.tragepro.api.data.model.response.dhan.DhanOiPacket;
import com.tragepro.api.data.model.response.dhan.DhanPrevClosePacket;
import com.tragepro.api.data.model.response.dhan.DhanQuotePacket;
import com.tragepro.api.data.model.response.dhan.DhanTickerPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

/**
 * Concrete implementation of the AbstractWebSocket client for Dhan HQ Feed APIs.
 */
@Slf4j
@Component
public class WebSocketImpl extends AbstractWebSocket<DhanWebSocketRequest> {

    private final DhanFeedProperties properties;

    public WebSocketImpl(WebSocketClient webSocketClient, ObjectMapper objectMapper, DhanFeedProperties properties) {
        super(webSocketClient, objectMapper);
        this.properties = properties;
    }

    /**
     * Factory method for creating DhanWebSocket instances.
     */
    public static AbstractWebSocket<DhanWebSocketRequest> createWebSocket(
            WebSocketClient webSocketClient, ObjectMapper objectMapper, DhanFeedProperties properties) {
        return new WebSocketImpl(webSocketClient, objectMapper, properties);
    }

    /**
     * Connects to Dhan WebSocket using properties configured in application-feed.yml.
     * Maps to: wss://api-feed.dhan.co?version=2&token=TOKEN&clientId=CLIENT_ID&authType=2
     */
    public CompletableFuture<WebSocketSession> connect() {
        String url = String.format(
                "%s?version=2&token=%s&clientId=%s&authType=2",
                properties.getWebsocketUrl(), properties.getAccessToken(), properties.getClientId());
        log.info("Connecting to Dhan WebSocket feed using properties-based URL...");
        return connect(url);
    }

    @Override
    protected void onOpen(WebSocketSession session) {
        log.info("Dhan WebSocket session opened: {}", session.getId());
    }

    @Override
    protected void onMessage(String message) {
        log.debug("Dhan WebSocket received text message: {}", message);
    }

    @Override
    protected void onBinaryMessage(ByteBuffer message) {
        log.debug("Dhan WebSocket received binary message of size: {}", message.remaining());
        try {
            message.order(ByteOrder.LITTLE_ENDIAN);

            if (message.remaining() < 8) {
                log.warn(
                        "Invalid message size, header must be at least 8 bytes. Received: {} bytes",
                        message.remaining());
                return;
            }

            byte responseCode = message.get();
            short messageLength = message.getShort();
            byte exchangeSegment = message.get();
            int securityId = message.getInt();

            DhanHeader header = DhanHeader.builder()
                    .responseCode(responseCode)
                    .messageLength(messageLength)
                    .exchangeSegment(exchangeSegment)
                    .securityId(securityId)
                    .build();

            switch (responseCode) {
                case 2: // Ticker Packet
                    if (message.remaining() < 8) {
                        log.warn(
                                "Invalid Ticker packet size. Expected 8 bytes remaining, got: {}", message.remaining());
                        return;
                    }
                    float lastTradedPrice = message.getFloat();
                    int lastTradeTime = message.getInt();

                    DhanTickerPacket tickerPacket = DhanTickerPacket.builder()
                            .header(header)
                            .lastTradedPrice(lastTradedPrice)
                            .lastTradeTime(lastTradeTime)
                            .build();

                    log.trace("Parsed Ticker Packet: {}", tickerPacket);
                    notifyListenersTicker(tickerPacket);
                    break;

                case 4: // Quote Packet
                    if (message.remaining() < 42) {
                        log.warn(
                                "Invalid Quote packet size. Expected 42 bytes remaining, got: {}", message.remaining());
                        return;
                    }
                    float qLastTradedPrice = message.getFloat();
                    short qLastTradedQuantity = message.getShort();
                    int qLastTradeTime = message.getInt();
                    float qAverageTradePrice = message.getFloat();
                    int qVolume = message.getInt();
                    int qTotalSellQuantity = message.getInt();
                    int qTotalBuyQuantity = message.getInt();
                    float qOpen = message.getFloat();
                    float qClose = message.getFloat();
                    float qHigh = message.getFloat();
                    float qLow = message.getFloat();

                    DhanQuotePacket quotePacket = DhanQuotePacket.builder()
                            .header(header)
                            .lastTradedPrice(qLastTradedPrice)
                            .lastTradedQuantity(qLastTradedQuantity)
                            .lastTradeTime(qLastTradeTime)
                            .averageTradePrice(qAverageTradePrice)
                            .volume(qVolume)
                            .totalSellQuantity(qTotalSellQuantity)
                            .totalBuyQuantity(qTotalBuyQuantity)
                            .open(qOpen)
                            .close(qClose)
                            .high(qHigh)
                            .low(qLow)
                            .build();

                    log.trace("Parsed Quote Packet: {}", quotePacket);
                    notifyListenersQuote(quotePacket);
                    break;

                case 5: // OI Packet
                    if (message.remaining() < 4) {
                        log.warn("Invalid OI packet size. Expected 4 bytes remaining, got: {}", message.remaining());
                        return;
                    }
                    int openInterest = message.getInt();

                    DhanOiPacket oiPacket = DhanOiPacket.builder()
                            .header(header)
                            .openInterest(openInterest)
                            .build();

                    log.trace("Parsed OI Packet: {}", oiPacket);
                    notifyListenersOi(oiPacket);
                    break;

                case 6: // Prev Close Packet
                    if (message.remaining() < 8) {
                        log.warn(
                                "Invalid Prev Close packet size. Expected 8 bytes remaining, got: {}",
                                message.remaining());
                        return;
                    }
                    float prevClosePrice = message.getFloat();
                    int prevOpenInterest = message.getInt();

                    DhanPrevClosePacket prevClosePacket = DhanPrevClosePacket.builder()
                            .header(header)
                            .prevClosePrice(prevClosePrice)
                            .prevOpenInterest(prevOpenInterest)
                            .build();

                    log.trace("Parsed Prev Close Packet: {}", prevClosePacket);
                    notifyListenersPrevClose(prevClosePacket);
                    break;

                case 8: // Full Packet
                    if (message.remaining() < 154) {
                        log.warn(
                                "Invalid Full packet size. Expected 154 bytes remaining, got: {}", message.remaining());
                        return;
                    }
                    float fLastTradedPrice = message.getFloat();
                    short fLastTradedQuantity = message.getShort();
                    int fLastTradeTime = message.getInt();
                    float fAverageTradePrice = message.getFloat();
                    int fVolume = message.getInt();
                    int fTotalSellQuantity = message.getInt();
                    int fTotalBuyQuantity = message.getInt();
                    int fOpenInterest = message.getInt();
                    int fHighOpenInterest = message.getInt();
                    int fLowOpenInterest = message.getInt();
                    float fOpen = message.getFloat();
                    float fClose = message.getFloat();
                    float fHigh = message.getFloat();
                    float fLow = message.getFloat();

                    List<DhanDepthLevel> depthLevels = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        int bidQuantity = message.getInt();
                        int askQuantity = message.getInt();
                        short bidOrders = message.getShort();
                        short askOrders = message.getShort();
                        float bidPrice = message.getFloat();
                        float askPrice = message.getFloat();

                        depthLevels.add(DhanDepthLevel.builder()
                                .bidQuantity(bidQuantity)
                                .askQuantity(askQuantity)
                                .bidOrders(bidOrders)
                                .askOrders(askOrders)
                                .bidPrice(bidPrice)
                                .askPrice(askPrice)
                                .build());
                    }

                    DhanFullPacket fullPacket = DhanFullPacket.builder()
                            .header(header)
                            .lastTradedPrice(fLastTradedPrice)
                            .lastTradedQuantity(fLastTradedQuantity)
                            .lastTradeTime(fLastTradeTime)
                            .averageTradePrice(fAverageTradePrice)
                            .volume(fVolume)
                            .totalSellQuantity(fTotalSellQuantity)
                            .totalBuyQuantity(fTotalBuyQuantity)
                            .openInterest(fOpenInterest)
                            .highOpenInterest(fHighOpenInterest)
                            .lowOpenInterest(fLowOpenInterest)
                            .open(fOpen)
                            .close(fClose)
                            .high(fHigh)
                            .low(fLow)
                            .marketDepth(depthLevels)
                            .build();

                    log.trace("Parsed Full Packet: {}", fullPacket);
                    notifyListenersFull(fullPacket);
                    break;

                default:
                    log.warn("Unsupported Dhan feed response code: {}", responseCode);
                    break;
            }
        } catch (Exception e) {
            log.error("Error parsing Dhan binary message", e);
        }
    }

    private void notifyListenersTicker(DhanTickerPacket packet) {
        getListeners().forEach(listener -> {
            try {
                listener.onDhanTicker(packet);
            } catch (Exception e) {
                log.error("Error dispatching DhanTickerPacket to listener", e);
            }
        });
    }

    private void notifyListenersQuote(DhanQuotePacket packet) {
        getListeners().forEach(listener -> {
            try {
                listener.onDhanQuote(packet);
            } catch (Exception e) {
                log.error("Error dispatching DhanQuotePacket to listener", e);
            }
        });
    }

    private void notifyListenersOi(DhanOiPacket packet) {
        getListeners().forEach(listener -> {
            try {
                listener.onDhanOi(packet);
            } catch (Exception e) {
                log.error("Error dispatching DhanOiPacket to listener", e);
            }
        });
    }

    private void notifyListenersPrevClose(DhanPrevClosePacket packet) {
        getListeners().forEach(listener -> {
            try {
                listener.onDhanPrevClose(packet);
            } catch (Exception e) {
                log.error("Error dispatching DhanPrevClosePacket to listener", e);
            }
        });
    }

    private void notifyListenersFull(DhanFullPacket packet) {
        getListeners().forEach(listener -> {
            try {
                listener.onDhanFull(packet);
            } catch (Exception e) {
                log.error("Error dispatching DhanFullPacket to listener", e);
            }
        });
    }

    @Override
    protected void onClose(CloseStatus status) {
        log.info("Dhan WebSocket session closed: {}", status);
    }

    @Override
    protected void onError(Throwable exception) {
        log.error("Dhan WebSocket error occurred: ", exception);
    }
}
