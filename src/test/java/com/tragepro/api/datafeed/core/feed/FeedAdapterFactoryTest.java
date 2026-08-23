package com.tragepro.api.datafeed.core.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tragepro.api.common.exception.AppException;
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
        DataFeedAdapter adapter = factory.get();
        assertEquals(dataFeedAdapter, adapter);
    }

    @Test
    void testGet_EmptyAdapters_ThrowsAppException() {
        FeedAdapterFactory factory = new FeedAdapterFactory(null);
        assertThrows(AppException.class, factory::get);
    }

    @Test
    void testGet_NullAdapters_ThrowsAppException() {
        FeedAdapterFactory factory = new FeedAdapterFactory(null);
        assertThrows(AppException.class, factory::get);
    }
}
