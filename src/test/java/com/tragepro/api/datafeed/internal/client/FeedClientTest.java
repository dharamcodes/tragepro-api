package com.tragepro.api.datafeed.internal.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.FeedClientRequest;
import com.tragepro.api.datafeed.dto.FeedClientResponse;
import com.tragepro.api.datafeed.internal.client.adapter.client.DummyFeedAdapter;
import com.tragepro.api.datafeed.internal.client.adapter.client.FeedClientAdapter;
import com.tragepro.api.datafeed.internal.client.mapper.FeedClientMapper;
import java.net.http.HttpClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest
@ActiveProfiles("test")
class FeedClientTest extends ContainerConfig {

  @Autowired private FeedClientMapper feedClientMapper;
  @Autowired private DummyFeedAdapter dummyFeedAdapter;

  private FeedClient feedClient;
  private FeedClientAdapter feedClientAdapter;

  @BeforeEach
  void setUpClient() {
    HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    ClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
    RestClient restClient =
        RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(wireMockServer.baseUrl())
            .build();
    HttpServiceProxyFactory factory =
        HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
    feedClient = factory.createClient(FeedClient.class);
    feedClientAdapter = new FeedClientAdapter(feedClient, feedClientMapper);
  }

  @Test
  void testFeedClientDirectHttpCallsToWireMock() {
    FeedClientRequest req =
        FeedClientRequest.builder()
            .securityId(101)
            .instrument("AAPL")
            .fromDate("2026-01-01")
            .toDate("2026-01-02")
            .build();

    FeedClientResponse histResp = feedClient.getHistoricalFeed(req);
    assertNotNull(histResp);
    assertNotNull(histResp.close());
    assertFalse(histResp.close().isEmpty());

    FeedClientResponse intraResp = feedClient.getIntradayFeed(req);
    assertNotNull(intraResp);
    assertNotNull(intraResp.close());
    assertFalse(intraResp.close().isEmpty());
  }

  @Test
  void testFeedClientAdapterWithWireMock() {
    FeedClientRequest req =
        FeedClientRequest.builder()
            .securityId(101)
            .instrument("AAPL")
            .fromDate("2026-01-01")
            .toDate("2026-01-02")
            .build();

    List<CandleRequest> histList = feedClientAdapter.historicalDataAdapter(req);
    assertNotNull(histList);
    assertFalse(histList.isEmpty());
    assertEquals("AAPL", histList.get(0).symbolData().name());

    List<CandleRequest> intraList = feedClientAdapter.intradayDataAdapter(req);
    assertNotNull(intraList);
    assertFalse(intraList.isEmpty());
    assertEquals("AAPL", intraList.get(0).symbolData().name());
  }

  @Test
  void testDummyFeedAdapter() {
    FeedClientRequest req =
        FeedClientRequest.builder()
            .securityId(101)
            .instrument("AAPL")
            .fromDate("2026-01-01")
            .toDate("2026-01-02")
            .build();

    List<CandleRequest> histList = dummyFeedAdapter.historicalDataAdapter(req);
    assertNotNull(histList);
    assertFalse(histList.isEmpty());

    List<CandleRequest> intraList = dummyFeedAdapter.intradayDataAdapter(req);
    assertNotNull(intraList);
    assertFalse(intraList.isEmpty());

    assertNotNull(dummyFeedAdapter.apiCallFallbackHistorical(req));
    assertNotNull(dummyFeedAdapter.apiCallFallbackIntraday(req));
    assertNotNull(feedClientAdapter.apiCallFallbackHistorical(req));
    assertNotNull(feedClientAdapter.apiCallFallbackIntraday(req));
  }
}
