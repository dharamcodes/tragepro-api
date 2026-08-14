package com.tragepro.api.datafeed.internal;

import com.tragepro.api.datafeed.model.response.SecurityResponse;

interface SecurityService {
  SecurityResponse fetSecurityBySymbol(String symbol);
}
