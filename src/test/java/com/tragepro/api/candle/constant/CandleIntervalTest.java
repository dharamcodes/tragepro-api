package com.tragepro.api.candle.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CandleIntervalTest {

    @Test
    void testEnumValuesAndGetters() {
        assertEquals(1, CandleInterval.ONE_SECOND.getInterval());
        assertEquals(TimeUnit.SECOND, CandleInterval.ONE_SECOND.getTimeUnit());
        assertEquals("1s", CandleInterval.ONE_SECOND.getValue());

        assertEquals(30, CandleInterval.THIRTY_SECONDS.getInterval());
        assertEquals(TimeUnit.SECOND, CandleInterval.THIRTY_SECONDS.getTimeUnit());
        assertEquals("30s", CandleInterval.THIRTY_SECONDS.getValue());

        assertEquals(1, CandleInterval.ONE_MINUTE.getInterval());
        assertEquals(TimeUnit.MINUTE, CandleInterval.ONE_MINUTE.getTimeUnit());
        assertEquals("1m", CandleInterval.ONE_MINUTE.getValue());

        assertEquals(5, CandleInterval.FIVE_MINUTES.getInterval());
        assertEquals(TimeUnit.MINUTE, CandleInterval.FIVE_MINUTES.getTimeUnit());
        assertEquals("5m", CandleInterval.FIVE_MINUTES.getValue());

        assertEquals(15, CandleInterval.FIFTEEN_MINUTES.getInterval());
        assertEquals(TimeUnit.MINUTE, CandleInterval.FIFTEEN_MINUTES.getTimeUnit());
        assertEquals("15m", CandleInterval.FIFTEEN_MINUTES.getValue());

        assertEquals(30, CandleInterval.THIRTY_MINUTES.getInterval());
        assertEquals(TimeUnit.MINUTE, CandleInterval.THIRTY_MINUTES.getTimeUnit());
        assertEquals("30m", CandleInterval.THIRTY_MINUTES.getValue());

        assertEquals(1, CandleInterval.ONE_HOUR.getInterval());
        assertEquals(TimeUnit.HOUR, CandleInterval.ONE_HOUR.getTimeUnit());
        assertEquals("1h", CandleInterval.ONE_HOUR.getValue());

        assertEquals(4, CandleInterval.FOUR_HOURS.getInterval());
        assertEquals(TimeUnit.HOUR, CandleInterval.FOUR_HOURS.getTimeUnit());
        assertEquals("4h", CandleInterval.FOUR_HOURS.getValue());

        assertEquals(1, CandleInterval.ONE_DAY.getInterval());
        assertEquals(TimeUnit.DAY, CandleInterval.ONE_DAY.getTimeUnit());
        assertEquals("1d", CandleInterval.ONE_DAY.getValue());
    }

    @Test
    void testEnumLength() {
        assertEquals(9, CandleInterval.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(CandleInterval.ONE_SECOND, CandleInterval.valueOf("ONE_SECOND"));
        assertEquals(CandleInterval.ONE_DAY, CandleInterval.valueOf("ONE_DAY"));
    }
}
