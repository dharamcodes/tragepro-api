package com.tragepro.api.domain.strategy.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IndicatorType {
    VOLUME_PROFILE("VOLUME_PROFILE"),
    VWAP_LEVELS("VWAP_LEVELS"),
    LIQUIDITY_LEVELS("LIQUIDITY_LEVELS"),
    BID_ASK_LEVELS("BID_ASK_LEVELS"),
    OPENING_RANGE_BREAKOUT("OPENING_RANGE_BREAKOUT"),
    AMD_ABSORPTION("AMD_ABSORPTION"),
    ORDER_BLOCKS("ORDER_BLOCKS"),
    LIQUIDITY_ZONES("LIQUIDITY_ZONES"),
    ORDER_FLOW("ORDER_FLOW"),
    TIME_AND_SALES("TIME_AND_SALES");

    private final String name;

    public static IndicatorType of(String name) {
        return Arrays.stream(values())
                .filter(type -> type.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown IndicatorType: " + name));
    }
}
