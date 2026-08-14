package com.tragepro.api.datafeed.model;

import com.tragepro.api.datafeed.constant.DatafeedState;
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
