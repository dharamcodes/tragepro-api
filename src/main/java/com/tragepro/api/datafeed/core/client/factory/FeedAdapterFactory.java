package com.tragepro.api.datafeed.core.client.factory;

import com.tragepro.api.datafeed.core.client.DataFeedAdapter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedAdapterFactory {

    private final DataFeedAdapter adapter;

    public Optional<DataFeedAdapter> get() {
        return Optional.ofNullable(adapter);
    }
}
