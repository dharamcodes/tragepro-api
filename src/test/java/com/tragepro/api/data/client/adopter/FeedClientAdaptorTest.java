package com.tragepro.api.data.client.adopter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.data.client.FeedClient;
import com.tragepro.api.data.client.mapper.FeedClientMapper;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.FeedClientResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedClientAdaptorTest {

  @Mock private FeedClient feedClient;
  @Mock private FeedClientMapper feedClientMapper;

  @InjectMocks private FeedClientAdaptor adaptor;

  private FeedClientRequest request;
  private FeedClientResponse response;
  private List<CandleRequest> mappedRequests;

  @BeforeEach
  void setUp() {
    request = FeedClientRequest.builder().securityId(123).build();
    response = new FeedClientResponse(null, null, null, null, null, null, null);
    mappedRequests = List.of(CandleRequest.builder().build());
  }

  @Test
  void testHistoricalDataAdaptor() {
    when(feedClient.getHistoricalFeed(request)).thenReturn(response);
    when(feedClientMapper.map(response, request)).thenReturn(mappedRequests);

    List<CandleRequest> result = adaptor.historicalDataAdaptor(request);

    assertEquals(mappedRequests, result);
    verify(feedClient).getHistoricalFeed(request);
    verify(feedClientMapper).map(response, request);
  }

  @Test
  void testIntradayDataAdaptor() {
    when(feedClient.getIntradayFeed(request)).thenReturn(response);
    when(feedClientMapper.map(response, request)).thenReturn(mappedRequests);

    List<CandleRequest> result = adaptor.intradayDataAdaptor(request);

    assertEquals(mappedRequests, result);
    verify(feedClient).getIntradayFeed(request);
    verify(feedClientMapper).map(response, request);
  }

  @Test
  void testFallbackMethods() throws Exception {
    when(feedClient.getHistoricalFeed(request)).thenReturn(response);
    when(feedClientMapper.map(response, request)).thenReturn(mappedRequests);

    List<CandleRequest> resultHist =
        org.springframework.test.util.ReflectionTestUtils.invokeMethod(
            adaptor, "apiCallFallbackHistorical", request);
    assertEquals(mappedRequests, resultHist);

    when(feedClient.getIntradayFeed(request)).thenReturn(response);
    List<CandleRequest> resultIntra =
        org.springframework.test.util.ReflectionTestUtils.invokeMethod(
            adaptor, "apiCallFallbackIntraday", request);
    assertEquals(mappedRequests, resultIntra);
  }
}
