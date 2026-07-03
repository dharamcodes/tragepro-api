package com.tragepro.api.strategy.util;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.strategy.model.TimeframeModel;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeframeConverterUtil {

  public static List<CandleData> convert(
      List<CandleData> candles, TimeframeModel source, TimeframeModel destination) {

    if (destination.getValue() < source.getValue()) {
      throw new AppException(ErrorType.INVALID_FIELD_TYPE);
    }

    return candles.stream()
        .collect(
            Collectors.groupingBy(
                c -> bucketStart(c.timestamp(), destination), TreeMap::new, Collectors.toList()))
        .entrySet()
        .stream()
        .map(TimeframeConverterUtil::aggregate)
        .toList();
  }

  private static CandleData aggregate(Map.Entry<Long, List<CandleData>> entry) {

    List<CandleData> candles = entry.getValue();

    CandleData first = candles.getFirst();
    CandleData last = candles.getLast();

    return new CandleData(
        entry.getKey(),
        first.open(),
        candles.stream().map(CandleData::high).max(Double::compareTo).orElse(0.0),
        candles.stream().map(CandleData::low).min(Double::compareTo).orElse(0.0),
        last.close(),
        candles.stream().map(CandleData::volume).reduce(0.0, Double::sum));
  }

  private static long bucketStart(long timeStamp, TimeframeModel tf) {
    ZonedDateTime time = Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault());

    return switch (tf.getUom()) {
      case MINUTE -> {
        int bucketMinute = (time.getMinute() / tf.getValue()) * tf.getValue();
        yield time.truncatedTo(ChronoUnit.HOURS)
            .withMinute(bucketMinute)
            .toInstant()
            .toEpochMilli();
      }
      case HOUR -> {
        int bucketHour = (time.getHour() / tf.getValue()) * tf.getValue();
        yield time.truncatedTo(ChronoUnit.DAYS).withHour(bucketHour).toInstant().toEpochMilli();
      }
      case DAY -> {
        long bucketDay = (time.toLocalDate().toEpochDay() / tf.getValue()) * tf.getValue();
        yield LocalDate.ofEpochDay(bucketDay)
            .atStartOfDay(time.getZone())
            .toInstant()
            .toEpochMilli();
      }
      case WEEK ->
          time.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli();
      case MONTH -> {
        int bucketMonth = ((time.getMonthValue() - 1) / tf.getValue()) * tf.getValue() + 1;
        yield ZonedDateTime.of(
                LocalDate.of(time.getYear(), bucketMonth, 1), LocalTime.MIN, time.getZone())
            .toInstant()
            .toEpochMilli();
      }
    };
  }
}
