package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * Dhan feed response packet containing Previous day close data (Response Code 6).
 */
@Data
@Builder
public class DhanPrevClosePacket {
    private DhanHeader header;
    private float prevClosePrice;
    private int prevOpenInterest;
}
