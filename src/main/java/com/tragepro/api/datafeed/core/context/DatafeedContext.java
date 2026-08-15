package com.tragepro.api.datafeed.core.context;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.datafeed.DatafeedModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
   * Atomically updates the state of an existing datafeed entry. Throws {@link AppException} if the
   * key is not found to avoid silent lost-update bugs.
   */
  public void updateStatus(SymbolDataModel key, DatafeedState state) {
    DatafeedModel updated =
        datafeedContext.computeIfPresent(
            key,
            (k, existing) ->
                DatafeedModel.builder()
                    .symbol(existing.getSymbol())
                    .timestamp(existing.getTimestamp())
                    .state(state)
                    .build());
    if (updated == null) {
      throw new AppException(ErrorType.INTERNAL_ERROR);
    }
  }
}
