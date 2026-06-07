package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * 8-byte header prefixed to all Dhan response packets.
 */
@Data
@Builder
public class DhanHeader {
    private byte responseCode;
    private short messageLength;
    private byte exchangeSegment;
    private int securityId;
}
