package com.tragepro.api.strategy.model;

import com.tragepro.api.common.model.CandleData;
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
  private List<CandleData> tickDatta;
  private List<CandleData> htfData;
  private List<CandleData> ltfData;
}
