package com.tragepro.api.datafeed.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.response.FeedClientResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class DataFeedRestClientMapperTest {

    private final FeedClientMapper mapper = new FeedClientMapper() {};

    @Test
    void testMap_NullResponseOrRequest() {
        assertTrue(mapper.map(null, FeedClientRequest.builder().build()).isEmpty());
        assertTrue(mapper.map(new FeedClientResponse(null, null, null, null, null, null, null), null)
                .isEmpty());
    }

    @Test
    void testMap_EmptyTimestamp() {
        FeedClientResponse response =
                new FeedClientResponse(null, null, null, null, null, Collections.emptyList(), null);
        assertTrue(mapper.map(response, FeedClientRequest.builder().build()).isEmpty());
    }

    @Test
    void testMap_Success() {
        FeedClientRequest request =
                FeedClientRequest.builder().instrument("EQUITY").build();
        FeedClientResponse response = new FeedClientResponse(
                List.of(100.0, 101.0),
                List.of(105.0, 106.0),
                List.of(99.0, 100.0),
                List.of(102.0, 103.0),
                List.of(1000L, 2000L),
                List.of(1600000000L, 1600000060L),
                null);

        List<CandleRequest> result = mapper.map(response, request);

        assertEquals(2, result.size());
        assertEquals("EQUITY", result.get(0).symbolData().name());
        assertEquals(1600000000L, result.get(0).candleData().timestamp());
        assertEquals(100.0, result.get(0).candleData().open());
        assertEquals(1000L, result.get(0).candleData().volume());
    }

    @Test
    void testMap_MissingValuesFallback() {
        FeedClientRequest request =
                FeedClientRequest.builder().instrument("EQUITY").build();
        FeedClientResponse response = new FeedClientResponse(null, null, null, null, null, List.of(1600000000L), null);

        List<CandleRequest> result = mapper.map(response, request);

        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).candleData().open());
        assertEquals(0L, result.get(0).candleData().volume());
    }
}
