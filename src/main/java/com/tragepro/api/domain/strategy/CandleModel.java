package com.tragepro.api.domain.strategy;

import com.tragepro.api.domain.datafeed.CandleDataModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandleModel {
  private List<CandleDataModel> baseData;
  private List<CandleDataModel> htfData;
  private List<CandleDataModel> ltfData;
}
