package com.tragepro.api.common.context;

import com.tragepro.api.common.constant.DatafeedState;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.model.DatafeedModel;
import com.tragepro.api.common.model.SymbolDataModel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class DatafeedContext {

  private final Map<SymbolDataModel, DatafeedModel> datafeedContext = new HashMap<>();

  public DatafeedModel get(SymbolDataModel key) {
    return datafeedContext.get(key);
  }

  public void put(SymbolDataModel key, DatafeedModel value) {
    datafeedContext.put(key, value);
  }

  public void updateStatus(SymbolDataModel key, DatafeedState state) {
    if (datafeedContext.containsKey(key)) {
      DatafeedModel value = datafeedContext.get(key);
      value.setState(state);
      datafeedContext.put(key, value);
    } else {
      throw new AppException(ErrorType.INTERNAL_ERROR);
    }
  }
}
