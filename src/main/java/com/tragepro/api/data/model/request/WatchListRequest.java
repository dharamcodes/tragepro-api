package com.tragepro.api.data.model.request;

import com.tragepro.api.data.model.SymbolData;
import java.util.Set;
import lombok.Builder;

@Builder
public record WatchListRequest(String name, String description, Set<SymbolData> stocks) {
    public WatchListRequest() {
        this(null, null, null);
    }
}
