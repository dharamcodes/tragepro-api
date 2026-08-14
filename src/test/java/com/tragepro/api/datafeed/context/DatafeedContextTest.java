package com.tragepro.api.datafeed.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.datafeed.constant.DatafeedState;
import com.tragepro.api.datafeed.model.DatafeedModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatafeedContextTest {

  private DatafeedContext datafeedContext;

  @BeforeEach
  void setUp() {
    datafeedContext = new DatafeedContext();
  }

  @Test
  void testPutAndGet() {
    SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
    DatafeedModel model =
        DatafeedModel.builder().symbol("AAPL").state(DatafeedState.INITIALIZED).build();

    datafeedContext.put(symbol, model);
    assertEquals(model, datafeedContext.get(symbol));
  }

  @Test
  void testUpdateStatus_Success() {
    SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
    DatafeedModel model =
        DatafeedModel.builder().symbol("AAPL").state(DatafeedState.INITIALIZED).build();

    datafeedContext.put(symbol, model);
    datafeedContext.updateStatus(symbol, DatafeedState.COMPLETED);

    assertEquals(DatafeedState.COMPLETED, datafeedContext.get(symbol).getState());
  }

  @Test
  void testUpdateStatus_NotFound_ThrowsException() {
    SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
    assertThrows(
        AppException.class, () -> datafeedContext.updateStatus(symbol, DatafeedState.COMPLETED));
  }
}
