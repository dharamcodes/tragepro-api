package com.tragepro.api.data.model.request;

import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.model.SymbolData;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListRequest(
    String name, String description, Exchange exchange, Set<SymbolData> stocks) {}
