package com.tragepro.api.data.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tragepro.api.data.model.SymbolData;
import java.util.Set;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WatchListResponse(
    String id, String name, String description, Set<SymbolData> stocks) {}
