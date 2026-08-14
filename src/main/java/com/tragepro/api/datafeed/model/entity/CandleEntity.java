package com.tragepro.api.datafeed.model.entity;

import com.tragepro.api.common.model.entity.BaseEntity;
import com.tragepro.api.datafeed.model.CandleDataModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
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
@Document(collection = "candles")
public class CandleEntity extends BaseEntity {
  @Id private String id;

  private SymbolDataModel symbolData;
  private CandleDataModel candleData;
}
