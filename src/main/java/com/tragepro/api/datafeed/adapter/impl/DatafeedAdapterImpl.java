package com.tragepro.api.datafeed.adapter.impl;

import com.tragepro.api.datafeed.adapter.DatafeedAdapter;
import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatafeedAdapterImpl implements DatafeedAdapter {
    private final DatafeedService datafeedService;

    @Override
    public LoadCandleResponse loadData(LoadCandleRequest request) {
        return datafeedService.loadData(request);
    }
}
