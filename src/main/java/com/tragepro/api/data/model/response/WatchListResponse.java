package com.tragepro.api.data.model.response;

import com.tragepro.api.data.model.SymbolData;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListResponse(String id, String name, String description, Set<SymbolData> stocks) {}
