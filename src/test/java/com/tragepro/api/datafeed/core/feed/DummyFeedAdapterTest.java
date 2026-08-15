package com.tragepro.api.datafeed.core.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.datafeed.service.mapper.FeedClientMapper;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.response.FeedClientResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DummyFeedAdapterTest {

  @Mock private FeedClientMapper feedClientMapper;

  @InjectMocks private DummyFeedAdapter dummyFeedAdapter;

  private FeedClientRequest request;
  private List<CandleRequest> mappedRequests;

  @BeforeEach
  void setUp() {
    request = FeedClientRequest.builder().securityId(123).build();
    mappedRequests = List.of(CandleRequest.builder().build());
  }

  @Test
  void testHistoricalDataAdapter() {
    when(feedClientMapper.map(any(FeedClientResponse.class), eq(request)))
        .thenReturn(mappedRequests);

    List<CandleRequest> result = dummyFeedAdapter.historicalDataAdapter(request);

    assertEquals(mappedRequests, result);
    verify(feedClientMapper).map(any(FeedClientResponse.class), eq(request));
  }

  @Test
  void testIntradayDataAdapter() {
    when(feedClientMapper.map(any(FeedClientResponse.class), eq(request)))
        .thenReturn(mappedRequests);

    List<CandleRequest> result = dummyFeedAdapter.intradayDataAdapter(request);

    assertEquals(mappedRequests, result);
    verify(feedClientMapper).map(any(FeedClientResponse.class), eq(request));
  }

  @Test
  void testFallbackMethods() {
    Throwable cause = new RuntimeException("Rate limit");
    assertThrows(
        AppException.class,
        () ->
            ReflectionTestUtils.invokeMethod(
                dummyFeedAdapter, "apiCallFallbackHistorical", request, cause));

    assertThrows(
        AppException.class,
        () ->
            ReflectionTestUtils.invokeMethod(
                dummyFeedAdapter, "apiCallFallbackIntraday", request, cause));
  }
}
