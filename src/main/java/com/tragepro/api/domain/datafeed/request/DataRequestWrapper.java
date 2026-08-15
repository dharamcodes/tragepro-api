package com.tragepro.api.domain.datafeed.request;

import com.tragepro.api.domain.datafeed.SymbolDataModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataRequestWrapper {
  private SymbolDataModel symbolDataModel;
  ;
  private FeedClientRequest clientReq;
}
