package com.tragepro.api.domain.datafeed.request;

import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.Exchange;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListRequest(
    String name, String description, Exchange exchange, Set<SymbolDataModel> stocks) {}
