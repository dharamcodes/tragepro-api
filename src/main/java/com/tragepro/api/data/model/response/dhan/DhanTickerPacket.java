package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * Dhan feed response packet containing Ticker data (LTP and LTT) (Response Code 2).
 */
@Data
@Builder
public class DhanTickerPacket {
    private DhanHeader header;
    private float lastTradedPrice;
    private int lastTradeTime;
}
