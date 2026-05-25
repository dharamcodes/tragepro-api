package com.tragepro.api.candle.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeUnit {
    SECOND("Second"),
    MINUTE("Minute"),
    HOUR("Hour"),
    DAY("Day");
    private final String value;
}
