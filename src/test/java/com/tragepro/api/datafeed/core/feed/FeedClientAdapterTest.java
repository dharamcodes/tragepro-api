package com.tragepro.api.datafeed.core.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class FeedClientAdapterTest {

    @Mock
    private FeedClient feedClient;

    @Mock
    private FeedClientMapper feedClientMapper;

    @InjectMocks
    private FeedClientAdapter feedClientAdapter;

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
    void testHistoricalDataAdapter() {
        when(feedClient.getHistoricalFeed(request)).thenReturn(response);
        when(feedClientMapper.map(response, request)).thenReturn(mappedRequests);

        List<CandleRequest> result = feedClientAdapter.historicalDataAdapter(request);

        assertEquals(mappedRequests, result);
        verify(feedClient).getHistoricalFeed(request);
        verify(feedClientMapper).map(response, request);
    }

    @Test
    void testIntradayDataAdapter() {
        when(feedClient.getIntradayFeed(request)).thenReturn(response);
        when(feedClientMapper.map(response, request)).thenReturn(mappedRequests);

        List<CandleRequest> result = feedClientAdapter.intradayDataAdapter(request);

        assertEquals(mappedRequests, result);
        verify(feedClient).getIntradayFeed(request);
        verify(feedClientMapper).map(response, request);
    }

    @Test
    void testFallbackMethods() {
        Throwable cause = new RuntimeException("Rate limit");
        assertThrows(
                AppException.class,
                () -> ReflectionTestUtils.invokeMethod(feedClientAdapter, "apiCallFallbackHistorical", request, cause));

        assertThrows(
                AppException.class,
                () -> ReflectionTestUtils.invokeMethod(feedClientAdapter, "apiCallFallbackIntraday", request, cause));
    }
}
