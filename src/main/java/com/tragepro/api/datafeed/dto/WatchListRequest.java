package com.tragepro.api.datafeed.dto;

import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.model.SymbolDataModel;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListRequest(
    String name, String description, Exchange exchange, Set<SymbolDataModel> stocks) {}
