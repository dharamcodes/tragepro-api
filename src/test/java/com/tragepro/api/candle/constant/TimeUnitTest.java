package com.tragepro.api.candle.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimeUnitTest {

    @Test
    void testEnumValues() {
        assertEquals("Second", TimeUnit.SECOND.getValue());
        assertEquals("Minute", TimeUnit.MINUTE.getValue());
        assertEquals("Hour", TimeUnit.HOUR.getValue());
        assertEquals("Day", TimeUnit.DAY.getValue());
    }

    @Test
    void testEnumLength() {
        assertEquals(4, TimeUnit.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(TimeUnit.SECOND, TimeUnit.valueOf("SECOND"));
        assertEquals(TimeUnit.MINUTE, TimeUnit.valueOf("MINUTE"));
        assertEquals(TimeUnit.HOUR, TimeUnit.valueOf("HOUR"));
        assertEquals(TimeUnit.DAY, TimeUnit.valueOf("DAY"));
    }
}
