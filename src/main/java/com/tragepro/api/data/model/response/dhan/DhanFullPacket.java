package com.tragepro.api.data.model.response.dhan;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Dhan feed response packet containing complete Quote, OI and 5-level Market Depth (Response Code 8).
 */
@Data
@Builder
public class DhanFullPacket {
    private DhanHeader header;
    private float lastTradedPrice;
    private short lastTradedQuantity;
    private int lastTradeTime;
    private float averageTradePrice;
    private int volume;
    private int totalSellQuantity;
    private int totalBuyQuantity;
    private int openInterest;
    private int highOpenInterest;
    private int lowOpenInterest;
    private float open;
    private float close;
    private float high;
    private float low;
    private List<DhanDepthLevel> marketDepth;
}
