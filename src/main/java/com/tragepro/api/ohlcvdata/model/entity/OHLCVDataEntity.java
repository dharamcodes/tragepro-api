package com.tragepro.api.ohlcvdata.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import com.tragepro.api.ohlcvdata.model.OHLCVData;
import com.tragepro.api.ohlcvdata.model.SymbolData;
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
@Document(collection = "ohlcvData")
public class OHLCVDataEntity extends BaseEntity {
    @Id
    private String id;

    private SymbolData symbolData;
    private OHLCVData ohlcvData;
}
