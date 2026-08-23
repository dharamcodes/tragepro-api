package com.tragepro.api.datafeed.core.util;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.TimeframeModel;
import com.tragepro.api.domain.datafeed.constant.TimeUnit;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class TimeframesUtilTest {

    @Test
    void testConvert_InvalidTimeframe_ThrowsException() {
        TimeframeModel source =
                TimeframeModel.builder().value(5).uom(TimeUnit.MINUTE).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(1).uom(TimeUnit.MINUTE).build();

        AppException exception =
                assertThrows(AppException.class, () -> TimeframesUtil.convert(List.of(), source, destination));
        assertEquals(ErrorType.INVALID_FIELD_TYPE, exception.getErrorType());
    }

    @Test
    void testConvert_Success_Minute() {
        TimeframeModel source =
                TimeframeModel.builder().value(1).uom(TimeUnit.MINUTE).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(5).uom(TimeUnit.MINUTE).build();

        long now = Instant.parse("2026-07-01T10:02:00Z").toEpochMilli();
        long nowPlus1 = Instant.parse("2026-07-01T10:03:00Z").toEpochMilli();

        CandleDataModel c1 = new CandleDataModel(now, 100.0, 105.0, 95.0, 101.0, 10L);
        CandleDataModel c2 = new CandleDataModel(nowPlus1, 101.0, 106.0, 96.0, 102.0, 20L);

        List<CandleDataModel> result = TimeframesUtil.convert(List.of(c1, c2), source, destination);
        assertNotNull(result);
        assertEquals(1, result.size());
        CandleDataModel aggregate = result.get(0);
        assertEquals(100.0, aggregate.open());
        assertEquals(106.0, aggregate.high());
        assertEquals(95.0, aggregate.low());
        assertEquals(102.0, aggregate.close());
        assertEquals(30L, aggregate.volume());
    }

    @Test
    void testConvert_Hour() {
        TimeframeModel source =
                TimeframeModel.builder().value(1).uom(TimeUnit.HOUR).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(2).uom(TimeUnit.HOUR).build();
        long epoch = Instant.parse("2026-07-01T10:30:00Z").toEpochMilli();
        CandleDataModel c1 = new CandleDataModel(epoch, 100.0, 105.0, 95.0, 101.0, 10L);
        List<CandleDataModel> result = TimeframesUtil.convert(List.of(c1), source, destination);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testConvert_Day() {
        TimeframeModel source =
                TimeframeModel.builder().value(1).uom(TimeUnit.DAY).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(2).uom(TimeUnit.DAY).build();
        long epoch = Instant.parse("2026-07-01T10:30:00Z").toEpochMilli();
        CandleDataModel c1 = new CandleDataModel(epoch, 100.0, 105.0, 95.0, 101.0, 10L);
        List<CandleDataModel> result = TimeframesUtil.convert(List.of(c1), source, destination);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testConvert_Week() {
        TimeframeModel source =
                TimeframeModel.builder().value(1).uom(TimeUnit.WEEK).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(1).uom(TimeUnit.WEEK).build();
        long epoch = Instant.parse("2026-07-01T10:30:00Z").toEpochMilli();
        CandleDataModel c1 = new CandleDataModel(epoch, 100.0, 105.0, 95.0, 101.0, 10L);
        List<CandleDataModel> result = TimeframesUtil.convert(List.of(c1), source, destination);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testConvert_Month() {
        TimeframeModel source =
                TimeframeModel.builder().value(1).uom(TimeUnit.MONTH).build();
        TimeframeModel destination =
                TimeframeModel.builder().value(3).uom(TimeUnit.MONTH).build();
        long epoch = Instant.parse("2026-07-01T10:30:00Z").toEpochMilli();
        CandleDataModel c1 = new CandleDataModel(epoch, 100.0, 105.0, 95.0, 101.0, 10L);
        List<CandleDataModel> result = TimeframesUtil.convert(List.of(c1), source, destination);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
