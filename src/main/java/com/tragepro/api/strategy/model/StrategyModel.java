package com.tragepro.api.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyModel {
  private String name;
  private String desc;
  private String watchlist;
}
