package com.tragepro.api.common.model.request;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.common.model.SymbolData;
import org.junit.jupiter.api.Test;

class CandleRequestTest {

  @Test
  void testCandleRequestMethods() {
    SymbolData symbol = new SymbolData("AAPL", "Apple");
    CandleData candle = new CandleData(1609459200000L, 100.0, 105.0, 95.0, 101.0, 10.0);

    CandleRequest req = new CandleRequest(symbol, candle);
    assertNull(req.dataTimeType());
    assertEquals(symbol, req.symbolData());
    assertEquals(candle, req.candleData());

    CandleRequest added = req.add(DataTimeType.HISTORICAL);
    assertEquals(DataTimeType.HISTORICAL, added.dataTimeType());
    assertEquals(symbol, added.symbolData());

    SymbolData newSymbol = new SymbolData("MSFT", "Microsoft");
    CandleRequest setSymbol = req.setSymbolData(newSymbol);
    assertEquals(newSymbol, setSymbol.symbolData());
    assertNull(setSymbol.dataTimeType());

    CandleRequest withSymbol = req.withSymbolData(newSymbol);
    assertEquals(newSymbol, withSymbol.symbolData());
  }
}
