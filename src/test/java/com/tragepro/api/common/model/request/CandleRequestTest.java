package com.tragepro.api.common.model.request;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.model.CandleDataModel;
import com.tragepro.api.common.model.SymbolDataModel;
import org.junit.jupiter.api.Test;

class CandleRequestTest {

  @Test
  void testCandleRequestMethods() {
    SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple");
    CandleDataModel candle = new CandleDataModel(1609459200000L, 100.0, 105.0, 95.0, 101.0, 10.0);

    CandleRequest req = new CandleRequest(symbol, candle);
    assertNull(req.dataTimeType());
    assertEquals(symbol, req.symbolData());
    assertEquals(candle, req.candleData());

    CandleRequest added = req.add(DataTimeType.HISTORICAL);
    assertEquals(DataTimeType.HISTORICAL, added.dataTimeType());
    assertEquals(symbol, added.symbolData());

    SymbolDataModel newSymbol = new SymbolDataModel("MSFT", "Microsoft");
    CandleRequest setSymbol = req.setSymbolData(newSymbol);
    assertEquals(newSymbol, setSymbol.symbolData());
    assertNull(setSymbol.dataTimeType());

    CandleRequest withSymbol = req.withSymbolData(newSymbol);
    assertEquals(newSymbol, withSymbol.symbolData());
  }
}
