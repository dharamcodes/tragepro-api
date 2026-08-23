package com.tragepro.api.datafeed.core.feed;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedAdapterFactory {

    private final DataFeedAdapter adapter;

    public DataFeedAdapter get() {
        if (adapter == null) {
            log.error("No DataFeedAdapter implementation found. Check active Spring profile.");
            throw new AppException(ErrorType.INTERNAL_ERROR);
        }
        ;
        return adapter;
    }
}
