package com.tragepro.api.datafeed.model.request;

import com.tragepro.api.datafeed.constant.Exchange;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListRequest(
    String name, String description, Exchange exchange, Set<SymbolDataModel> stocks) {}
