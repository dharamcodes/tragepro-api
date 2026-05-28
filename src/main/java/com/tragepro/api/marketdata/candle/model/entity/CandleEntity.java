package com.tragepro.api.marketdata.candle.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import com.tragepro.api.marketdata.candle.model.CandleData;
import com.tragepro.api.marketdata.candle.model.SymbolData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "candleData")
public class CandleEntity extends BaseEntity {
    @Id
    private String id;

    private SymbolData symbolData;
    private CandleData candleData;
}
