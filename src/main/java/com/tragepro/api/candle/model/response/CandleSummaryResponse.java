package com.tragepro.api.candle.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandleSummaryResponse {

    private String id;
    private String symbolId;
    private String symbolName;
    private long timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
}
