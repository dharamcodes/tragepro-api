package com.tragepro.api.domain.strategy;

import com.tragepro.api.domain.datafeed.constant.Exchange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolModel {
  private String symbol;
  private String name;
  private Exchange exchange;
}
