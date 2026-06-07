package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * Representation of one level (bid/ask quotes) inside a Market Depth packet structure.
 */
@Data
@Builder
public class DhanDepthLevel {
    private int bidQuantity;
    private int askQuantity;
    private short bidOrders;
    private short askOrders;
    private float bidPrice;
    private float askPrice;
}
