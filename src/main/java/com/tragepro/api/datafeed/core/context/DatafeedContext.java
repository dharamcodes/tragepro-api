package com.tragepro.api.datafeed.core.context;

import com.tragepro.api.domain.datafeed.DatafeedModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("singleton")
public class DatafeedContext {

    private final Map<SymbolDataModel, DatafeedModel> datafeedContext = new ConcurrentHashMap<>();

    public DatafeedModel get(SymbolDataModel key) {
        return datafeedContext.get(key);
    }

    public void put(SymbolDataModel key, DatafeedModel value) {
        datafeedContext.put(key, value);
    }

    /**
     * Atomically transitions the datafeed state of a given symbol with an optional timestamp.
     *
     * @param symbol the symbol data model
     * @param state the target datafeed state
     * @param timestamp the optional timestamp date
     */
    public void transitionTo(SymbolDataModel symbol, DatafeedState state, LocalDate timestamp) {
        datafeedContext.compute(symbol, (key, existing) -> {
            if (existing == null) {
                log.info("Initializing data-client context state to {} for symbol: {}", state, symbol.symbol());
                return DatafeedModel.builder()
                        .symbol(symbol.symbol())
                        .timestamp(timestamp)
                        .state(state)
                        .build();
            }
            log.info("Updating data-client context state to {} for symbol: {}", state, symbol.symbol());
            return DatafeedModel.builder()
                    .symbol(existing.getSymbol())
                    .timestamp(timestamp != null ? timestamp : existing.getTimestamp())
                    .state(state)
                    .build();
        });
    }

    /**
     * Transitions the datafeed state of a given symbol without updating the timestamp.
     *
     * @param symbol the symbol data model
     * @param state the target datafeed state
     */
    public void transitionTo(SymbolDataModel symbol, DatafeedState state) {
        transitionTo(symbol, state, null);
    }
}
