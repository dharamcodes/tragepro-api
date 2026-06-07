package com.tragepro.api.data.model.response.dhan;

import lombok.Builder;
import lombok.Data;

/**
 * Dhan feed response packet containing Open Interest (OI) data (Response Code 5).
 */
@Data
@Builder
public class DhanOiPacket {
    private DhanHeader header;
    private int openInterest;
}
