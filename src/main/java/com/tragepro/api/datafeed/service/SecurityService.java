package com.tragepro.api.datafeed.service;

import com.tragepro.api.domain.datafeed.response.SecurityResponse;

public interface SecurityService {
    SecurityResponse fetSecurityBySymbol(String symbol);
}
