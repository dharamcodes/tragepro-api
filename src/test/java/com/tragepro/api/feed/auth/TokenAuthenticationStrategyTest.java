package com.tragepro.api.feed.auth;

import com.tragepro.api.marketdata.feed.auth.TokenAuthenticationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TokenAuthenticationStrategyTest {

    @Test
    void testApplyAuthentication() {
        TokenAuthenticationStrategy strategy = new TokenAuthenticationStrategy("my-token");
        HttpHeaders headers = new HttpHeaders();
        strategy.applyAuthentication(headers);
        assertEquals("Bearer my-token", headers.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void testApplyAuthentication_nullToken() {
        TokenAuthenticationStrategy strategy = new TokenAuthenticationStrategy(null);
        HttpHeaders headers = new HttpHeaders();
        strategy.applyAuthentication(headers);
        assertNull(headers.getFirst(HttpHeaders.AUTHORIZATION));
    }
}
