package com.tragepro.api.datafeed.adapter.impl;

import com.tragepro.api.datafeed.adapter.SecurityAdapter;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAdapterImpl implements SecurityAdapter {
    private final SecurityService securityService;

    @Override
    public SecurityResponse fetSecurityBySymbol(String symbol) {
        return securityService.fetSecurityBySymbol(symbol);
    }
}
