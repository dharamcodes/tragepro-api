package com.tragepro.api.datafeed.constant;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import org.junit.jupiter.api.Test;

class ConstantTest {

  @Test
  void testInstrumentType() {
    for (InstrumentType v : InstrumentType.values()) {
      assertNotNull(InstrumentType.valueOf(v.name()));
      assertNotNull(v.getValue());
      assertNotNull(v.getDetail());
      assertEquals(v, InstrumentType.of(v.getValue()));
    }
    assertThrows(AppException.class, () -> InstrumentType.of("INVALID_INSTRUMENT"));
  }

  @Test
  void testExchangeSeg() {
    for (ExchangeSeg v : ExchangeSeg.values()) {
      assertNotNull(ExchangeSeg.valueOf(v.name()));
      assertNotNull(v.getValue());
      assertNotNull(v.getExchange());
      assertNotNull(v.getSegment());
      assertTrue(v.getCode() >= 0);
      assertNotNull(ExchangeSeg.of(v.getExchange()));
    }
    assertThrows(AppException.class, () -> ExchangeSeg.of("INVALID_EXCHANGE"));
  }

  @Test
  void testTimeInterval() {
    for (TimeInterval v : TimeInterval.values()) {
      assertNotNull(TimeInterval.valueOf(v.name()));
      assertNotNull(v.getValue());
    }
  }

  @Test
  void testTimeUnit() {
    for (TimeUnit v : TimeUnit.values()) {
      assertNotNull(TimeUnit.valueOf(v.name()));
      assertEquals(v, TimeUnit.of(v.name()));
    }
    assertNull(TimeUnit.of(null));
    assertThrows(AppException.class, () -> TimeUnit.of("INVALID_UNIT"));
  }

  @Test
  void testOtherEnums() {
    for (Exchange v : Exchange.values()) {
      assertNotNull(Exchange.valueOf(v.name()));
    }
    for (Timeframe v : Timeframe.values()) {
      assertNotNull(Timeframe.valueOf(v.name()));
    }
    for (DatafeedState v : DatafeedState.values()) {
      assertNotNull(DatafeedState.valueOf(v.name()));
    }
  }
}
