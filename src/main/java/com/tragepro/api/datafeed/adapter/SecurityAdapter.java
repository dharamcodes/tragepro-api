package com.tragepro.api.datafeed.adapter;

import com.tragepro.api.domain.datafeed.response.SecurityResponse;

public interface SecurityAdapter {
    SecurityResponse fetSecurityBySymbol(String symbol);
}
