package com.tragepro.api.datafeed.core.feed;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedAdapterFactory {

  private final List<DataFeedAdapter> adapters;

  public DataFeedAdapter get() {
    if (adapters == null || adapters.isEmpty()) {
      log.error("No DataFeedAdapter implementation found. Check active Spring profile.");
      throw new AppException(ErrorType.INTERNAL_ERROR);
    }
    if (adapters.size() > 1) {
      log.warn(
          "Multiple DataFeedAdapter implementations found ({}). Using first: {}",
          adapters.size(),
          adapters.getFirst().getClass().getSimpleName());
    }
    DataFeedAdapter adapter = adapters.getFirst();
    log.debug("Resolved DataFeedAdapter: {}", adapter.getClass().getSimpleName());
    return adapter;
  }
}
