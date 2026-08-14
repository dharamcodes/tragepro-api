package com.tragepro.api.datafeed.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tragepro.api.common.exception.AppException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedAdapterFactoryTest {

  @Mock private DataFeedAdapter dataFeedAdapter;

  @Test
  void testGet_Success() {
    FeedAdapterFactory factory = new FeedAdapterFactory(List.of(dataFeedAdapter));
    DataFeedAdapter adapter = factory.get();
    assertEquals(dataFeedAdapter, adapter);
  }

  @Test
  void testGet_EmptyAdapters_ThrowsAppException() {
    FeedAdapterFactory factory = new FeedAdapterFactory(Collections.emptyList());
    assertThrows(AppException.class, factory::get);
  }

  @Test
  void testGet_NullAdapters_ThrowsAppException() {
    FeedAdapterFactory factory = new FeedAdapterFactory(null);
    assertThrows(AppException.class, factory::get);
  }

  @Test
  void testGet_MultipleAdapters() {
    DataFeedAdapter secondAdapter = org.mockito.Mockito.mock(DataFeedAdapter.class);
    FeedAdapterFactory factory = new FeedAdapterFactory(List.of(dataFeedAdapter, secondAdapter));
    DataFeedAdapter adapter = factory.get();
    assertEquals(dataFeedAdapter, adapter);
  }
}
