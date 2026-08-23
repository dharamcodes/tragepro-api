package com.tragepro.api.datafeed.core.client;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.datafeed.core.client.factory.FeedAdapterFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedAdapterFactoryTest {

    @Mock
    private DataFeedAdapter dataFeedAdapter;

    @Test
    void testGet_Success() {
        FeedAdapterFactory factory = new FeedAdapterFactory(dataFeedAdapter);
        Optional<DataFeedAdapter> adapter = factory.get();
        assertTrue(adapter.isPresent());
        assertEquals(dataFeedAdapter, adapter.get());
    }

    @Test
    void testGet_NullAdapter_ReturnsEmpty() {
        FeedAdapterFactory factory = new FeedAdapterFactory(null);
        Optional<DataFeedAdapter> adapter = factory.get();
        assertNotNull(adapter);
        assertTrue(adapter.isEmpty());
    }
}
