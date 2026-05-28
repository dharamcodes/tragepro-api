package com.tragepro.api.feed.auth;

import com.tragepro.api.marketdata.feed.auth.ApiKeyAuthenticationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiKeyAuthenticationStrategyTest {

    @Test
    void testApplyAuthentication() {
        ApiKeyAuthenticationStrategy strategy = new ApiKeyAuthenticationStrategy("X-API-KEY", "my-secret");
        HttpHeaders headers = new HttpHeaders();
        strategy.applyAuthentication(headers);
        assertEquals("my-secret", headers.getFirst("X-API-KEY"));
    }

    @Test
    void testApplyAuthentication_nullValues() {
        ApiKeyAuthenticationStrategy strategy = new ApiKeyAuthenticationStrategy(null, null);
        HttpHeaders headers = new HttpHeaders();
        strategy.applyAuthentication(headers);
        assertNull(headers.getFirst("X-API-KEY"));
    }
}
