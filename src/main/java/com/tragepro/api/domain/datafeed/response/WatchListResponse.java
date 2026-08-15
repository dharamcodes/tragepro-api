package com.tragepro.api.domain.datafeed.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.Exchange;
import java.util.Set;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WatchListResponse(
    String id, String name, String description, Exchange exchange, Set<SymbolDataModel> stocks) {}
