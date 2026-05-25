package com.tragepro.api.candle.provider.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.candle.event.CandleReceivedEvent;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class GenericBrokerMessageHandler implements BrokerMessageHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(String brokerName) {
        return "generic".equalsIgnoreCase(brokerName);
    }

    @Override
    public CandleReceivedEvent handleMessage(WebSocketSession session, String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);

            // Expected generic schema: {"symbol": "BTCUSD", "name": "Bitcoin", "timestamp": 123456, "open": 100,
            // "high": 110, "low": 90, "close": 105, "volume": 500}
            String symbolId = rootNode.path("symbol").asText();
            String symbolName = rootNode.path("name").asText(symbolId);
            long timestamp = rootNode.path("timestamp").asLong(System.currentTimeMillis());
            double open = rootNode.path("open").asDouble(0);
            double high = rootNode.path("high").asDouble(0);
            double low = rootNode.path("low").asDouble(0);
            double close = rootNode.path("close").asDouble(0);
            double volume = rootNode.path("volume").asDouble(0);

            Symbol symbol = new Symbol(symbolId, symbolName);
            Candle candle = new Candle(timestamp, open, high, low, close, volume);

            return new CandleReceivedEvent(symbol, candle);
        } catch (Exception e) {
            log.error("Failed to parse generic broker message: {}", message, e);
            return null;
        }
    }
}
