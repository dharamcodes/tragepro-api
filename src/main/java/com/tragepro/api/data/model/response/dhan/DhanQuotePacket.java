package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * Dhan feed response packet containing complete Quote data (Response Code 4).
 */
@Data
@Builder
public class DhanQuotePacket {
    private DhanHeader header;
    private float lastTradedPrice;
    private short lastTradedQuantity;
    private int lastTradeTime;
    private float averageTradePrice;
    private int volume;
    private int totalSellQuantity;
    private int totalBuyQuantity;
    private float open;
    private float close;
    private float high;
    private float low;
}
