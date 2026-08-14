package com.tragepro.api.datafeed.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tragepro.api.datafeed.constant.Exchange;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import java.util.Set;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WatchListResponse(
    String id, String name, String description, Exchange exchange, Set<SymbolDataModel> stocks) {}
