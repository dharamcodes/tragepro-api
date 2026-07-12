package com.tragepro.api.strategy.model;

import com.tragepro.api.common.constant.IndicatorType;
import com.tragepro.api.common.constant.Timeframe;
import com.tragepro.api.common.model.CandleDataModel;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorModel {
  private IndicatorType name;
  private Timeframe timeFrame;
  private Set<CandleDataModel> levels;
}
