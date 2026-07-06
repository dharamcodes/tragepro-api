package com.tragepro.api.datafeed.model.request;

import com.tragepro.api.common.model.SymbolDataModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataRequestWrapper {
  private SymbolDataModel symbolDataModel;
  ;
  private FeedClientRequest clientReq;
}
