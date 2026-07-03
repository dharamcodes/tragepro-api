package com.tragepro.api.data.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.model.SymbolData;
import java.util.Set;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WatchListResponse(
    String id, String name, String description, Exchange exchange, Set<SymbolData> stocks) {}
