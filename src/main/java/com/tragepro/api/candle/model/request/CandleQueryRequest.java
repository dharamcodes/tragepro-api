package com.tragepro.api.candle.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleQueryRequest {

    private String symbolId;

    private Long fromTimestamp;

    private Long toTimestamp;

    private Integer limit;
}
