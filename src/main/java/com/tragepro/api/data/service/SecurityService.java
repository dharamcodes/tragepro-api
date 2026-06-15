package com.tragepro.api.data.service;

import com.tragepro.api.data.model.response.SecurityResponse;

public interface SecurityService {
  SecurityResponse fetSecurityBySymbol(String symbol);
}
