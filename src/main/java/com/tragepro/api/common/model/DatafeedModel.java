package com.tragepro.api.common.model;

import com.tragepro.api.common.constant.DatafeedState;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatafeedModel {
  private String symbol;
  private LocalDate timestamp;
  private DatafeedState state;
}
