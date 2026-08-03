package com.tragepro.api.datafeed.internal.context;

import com.tragepro.api.common.constant.DatafeedState;
import com.tragepro.api.common.model.DatafeedModel;
import com.tragepro.api.common.model.SymbolDataModel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class DatafeedContext {

  private final Map<SymbolDataModel, DatafeedModel> context = new HashMap<>();

  public DatafeedModel get(SymbolDataModel name) {
    return context.get(name);
  }

  public void put(SymbolDataModel name, DatafeedModel datafeedModel) {
    context.put(name, datafeedModel);
  }

  public void updateStatus(SymbolDataModel name, DatafeedState state) {
    var item = context.get(name);
    if (item != null) {
      item.setState(state);
    }
  }
}
