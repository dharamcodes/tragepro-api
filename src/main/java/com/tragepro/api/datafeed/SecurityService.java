package com.tragepro.api.datafeed;

import com.tragepro.api.datafeed.model.response.SecurityResponse;

public interface SecurityService {
  SecurityResponse fetSecurityBySymbol(String symbol);
}
