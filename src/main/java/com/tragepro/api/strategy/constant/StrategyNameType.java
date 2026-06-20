package com.tragepro.api.strategy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyName {
    INTRADAY_VP_VWAP,
    SWING_VP_VWAP;
}
