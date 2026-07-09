package com.tragepro.api.strategy.util;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.model.CandleDataModel;
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

  public static List<CandleDataModel> convert(
      List<CandleDataModel> candles, TimeframeModel source, TimeframeModel destination) {

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

  private static CandleDataModel aggregate(Map.Entry<Long, List<CandleDataModel>> entry) {

    List<CandleDataModel> candles = entry.getValue();

    CandleDataModel first = candles.getFirst();
    CandleDataModel last = candles.getLast();

    return new CandleDataModel(
        entry.getKey(),
        first.open(),
        candles.stream().map(CandleDataModel::high).max(Double::compareTo).orElse(0.0),
        candles.stream().map(CandleDataModel::low).min(Double::compareTo).orElse(0.0),
        last.close(),
        candles.stream().mapToLong(CandleDataModel::volume).sum());
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
