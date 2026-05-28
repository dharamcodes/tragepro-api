package com.tragepro.api.feed.client;

import com.tragepro.api.marketdata.feed.auth.AuthenticationStrategy;
import com.tragepro.api.marketdata.feed.client.AbstractFeedRestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AbstractFeedRestClientTest {

    static class DummyRestClient extends AbstractFeedRestClient {
        boolean enrichCalled = false;

        public DummyRestClient(RestClient.Builder restClientBuilder, AuthenticationStrategy authenticationStrategy, String baseUrl) {
            super(restClientBuilder, authenticationStrategy, baseUrl);
        }

        @Override
        protected void enrichHeaders(HttpHeaders headers) {
            enrichCalled = true;
        }
    }

    @Test
    void testClientInitializationAndHeaders() {
        AuthenticationStrategy mockStrategy = Mockito.mock(AuthenticationStrategy.class);
        
        RestClient.Builder builder = RestClient.builder();
        DummyRestClient client = new DummyRestClient(builder, mockStrategy, "http://localhost");
        
        HttpHeaders headers = new HttpHeaders();
        client.applyHeaders(headers);
        
        verify(mockStrategy, Mockito.atLeastOnce()).applyAuthentication(headers);
        assertTrue(client.enrichCalled);
    }
}
