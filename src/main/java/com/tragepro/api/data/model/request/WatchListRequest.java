package com.tragepro.api.data.model.request;

import com.tragepro.api.data.model.SymbolData;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchListRequest {
    private String name;
    private String description;
    private Set<SymbolData> stocks;
}
