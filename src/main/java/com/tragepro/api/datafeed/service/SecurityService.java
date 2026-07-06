package com.tragepro.api.datafeed.service;

import com.tragepro.api.datafeed.model.response.SecurityResponse;

public interface SecurityService {
  SecurityResponse fetSecurityBySymbol(String symbol);
}
