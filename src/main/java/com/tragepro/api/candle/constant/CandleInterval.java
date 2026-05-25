package com.tragepro.api.candle.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CandleInterval {
    ONE_SECOND(1, TimeUnit.SECOND),
    THIRTY_SECONDS(30, TimeUnit.SECOND),
    ONE_MINUTE(1, TimeUnit.MINUTE),
    FIVE_MINUTES(5, TimeUnit.MINUTE),
    FIFTEEN_MINUTES(15, TimeUnit.MINUTE),
    THIRTY_MINUTES(30, TimeUnit.MINUTE),
    ONE_HOUR(1, TimeUnit.HOUR),
    FOUR_HOURS(4, TimeUnit.HOUR),
    ONE_DAY(1, TimeUnit.DAY);

    private final Integer interval;
    private final TimeUnit timeUnit;

    public String getValue() {
        return interval + timeUnit.name().substring(0, 1).toLowerCase();
    }
}
