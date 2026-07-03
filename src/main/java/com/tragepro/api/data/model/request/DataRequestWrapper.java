package com.tragepro.api.data.model.request;

import com.tragepro.api.common.model.SymbolData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataRequestWrapper {
  private SymbolData symbolData;
  ;
  private FeedClientRequest clientReq;
}
