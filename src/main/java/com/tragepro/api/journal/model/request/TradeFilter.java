package com.tragepro.api.journal.model.request;

import com.tragepro.api.journal.model.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeFilter {
  private String accountId;
  private Integer year;
  private Integer month;
  private Integer day;
  private String symbol;
  private TradeType tradeType;
}
