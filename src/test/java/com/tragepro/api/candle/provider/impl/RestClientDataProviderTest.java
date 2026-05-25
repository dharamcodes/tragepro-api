package com.tragepro.api.candle.provider.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.tragepro.api.candle.config.DataProviderProperties;
import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.request.CandleRequest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class RestClientDataProviderTest {

    private RestClientDataProvider dataProvider;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();

        DataProviderProperties props = new DataProviderProperties(
                Map.of(
                        "mockProvider",
                        new DataProviderProperties.ProviderConfig(
                                "https://api.mock.com",
                                "/v1/data?symbols={symbols}&interval={interval}",
                                "test-api-key",
                                null,
                                List.of("BTCUSD", "ETHUSD"),
                                30,
                                2,
                                "GET",
                                null)),
                null,
                null);

        dataProvider = new RestClientDataProvider(restClient, props);
    }

    @Test
    void testFetchAll_Success() {
        String mockResponse =
                """
                [
                    {
                        "symbolId": "BTCUSD",
                        "symbolName": "Bitcoin",
                        "timestamp": 1600000000,
                        "open": 20000,
                        "high": 21000,
                        "low": 19000,
                        "close": 20500,
                        "volume": 100
                    },
                    {
                        "symbolId": "ETHUSD",
                        "timestamp": 1600000000,
                        "open": 1500,
                        "high": 1600,
                        "low": 1400,
                        "close": 1550,
                        "volume": 500
                    }
                ]
                """;

        server.expect(requestTo("https://api.mock.com/v1/data?symbols=BTCUSD,ETHUSD&interval=1m"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "test-api-key"))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<CandleRequest> results = dataProvider.fetchAll(CandleInterval.ONE_MINUTE);

        assertNotNull(results);
        assertEquals(2, results.size());

        CandleRequest btc = results.get(0);
        assertEquals("BTCUSD", btc.getSymbol().id());
        assertEquals("Bitcoin", btc.getSymbol().name());
        assertEquals(20500.0, btc.getCandle().close());

        CandleRequest eth = results.get(1);
        assertEquals("ETHUSD", eth.getSymbol().id());
        assertEquals("ETHUSD", eth.getSymbol().name()); // Defaulted to id
        assertEquals(1550.0, eth.getCandle().close());

        server.verify();
    }

    @Test
    void testFetchAll_EmptyProviders() {
        DataProviderProperties emptyProps = new DataProviderProperties(Map.of(), null, null);
        RestClientDataProvider emptyProvider =
                new RestClientDataProvider(RestClient.builder().build(), emptyProps);

        List<CandleRequest> results = emptyProvider.fetchAll(CandleInterval.ONE_MINUTE);

        assertTrue(results.isEmpty());
    }

    @Test
    void testFetchAll_ApiError() {
        server.expect(requestTo("https://api.mock.com/v1/data?symbols=BTCUSD,ETHUSD&interval=1m"))
                .andRespond(withServerError());

        List<CandleRequest> results = dataProvider.fetchAll(CandleInterval.ONE_MINUTE);

        assertTrue(results.isEmpty()); // Handles exception and returns empty or partial list
        server.verify();
    }

    @Test
    void testFetchAll_EmptyResponse() {
        server.expect(requestTo("https://api.mock.com/v1/data?symbols=BTCUSD,ETHUSD&interval=1m"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        List<CandleRequest> results = dataProvider.fetchAll(CandleInterval.ONE_MINUTE);

        assertTrue(results.isEmpty());
        server.verify();
    }

    @Test
    void testFetchAll_PostMethod() {
        DataProviderProperties postProps = new DataProviderProperties(
                Map.of(
                        "mockProviderPost",
                        new DataProviderProperties.ProviderConfig(
                                "https://api.mock.com",
                                "/v1/data",
                                "test-api-key",
                                null,
                                List.of("XRPUSD"),
                                30,
                                1,
                                "POST",
                                "{\"symbols\":\"{symbols}\",\"interval\":\"{interval}\"}")),
                null,
                null);
        RestClientDataProvider postProvider =
                new RestClientDataProvider(RestClient.builder().build(), postProps);

        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer postServer = MockRestServiceServer.bindTo(builder).build();
        postProvider = new RestClientDataProvider(builder.build(), postProps);

        postServer
                .expect(requestTo("https://api.mock.com/v1/data"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"symbols\":\"XRPUSD\",\"interval\":\"1m\"}"))
                .andRespond(withSuccess("[{\"symbolId\": \"XRPUSD\", \"open\": 0.5}]", MediaType.APPLICATION_JSON));

        List<CandleRequest> results = postProvider.fetchAll(CandleInterval.ONE_MINUTE);
        assertEquals(1, results.size());
        assertEquals(0.5, results.get(0).getCandle().open());
        postServer.verify();
    }

    @Test
    void testFetchAll_AuthToken() {
        DataProviderProperties authProps = new DataProviderProperties(
                Map.of(
                        "mockProviderAuth",
                        new DataProviderProperties.ProviderConfig(
                                "https://api.mock.com",
                                "/v1/data?symbols={symbols}&interval={interval}",
                                null,
                                "test-auth-token",
                                List.of("BTCUSD"),
                                30,
                                2,
                                "GET",
                                null)),
                null,
                null);

        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer authServer = MockRestServiceServer.bindTo(builder).build();
        RestClientDataProvider authProvider = new RestClientDataProvider(builder.build(), authProps);

        authServer
                .expect(requestTo("https://api.mock.com/v1/data?symbols=BTCUSD&interval=1m"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer test-auth-token"))
                .andRespond(withSuccess("[{\"symbolId\": \"BTCUSD\", \"open\": 1.0}]", MediaType.APPLICATION_JSON));

        List<CandleRequest> results = authProvider.fetchAll(CandleInterval.ONE_MINUTE);
        assertEquals(1, results.size());
        authServer.verify();
    }

    @Test
    void testFetchAll_EmptySymbols() {
        DataProviderProperties emptySymbolsProps = new DataProviderProperties(
                Map.of(
                        "mockProviderEmpty",
                        new DataProviderProperties.ProviderConfig(
                                "https://api.mock.com",
                                "/v1/data",
                                "test-api-key",
                                null,
                                List.of(),
                                30,
                                1,
                                "GET",
                                null)),
                null,
                null);
        RestClientDataProvider emptySymbolsProvider =
                new RestClientDataProvider(RestClient.builder().build(), emptySymbolsProps);
        assertTrue(emptySymbolsProvider.fetchAll(CandleInterval.ONE_MINUTE).isEmpty());
    }
}
