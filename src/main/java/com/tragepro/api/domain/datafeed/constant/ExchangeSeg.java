package com.tragepro.api.domain.datafeed.constant;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExchangeSeg {
    IDX_I("IDX_I", "Index", "Index Value", 0),
    NSE_EQ("NSE_EQ", "NSE", "Equity Cash", 1),
    NSE_FNO("NSE_FNO", "NSE", "Futures & Options", 2),
    NSE_CURRENCY("NSE_CURRENCY", "NSE", "Currency", 3),
    BSE_EQ("BSE_EQ", "BSE", "Equity Cash", 4),
    MCX_COMM("MCX_COMM", "MCX", "Commodity", 5),
    BSE_CURRENCY("BSE_CURRENCY", "BSE", "Currency", 7),
    BSE_FNO("BSE_FNO", "BSE", "Futures & Options", 8);

    private final String value;
    private final String exchange;
    private final String segment;
    private final int code;

    public static ExchangeSeg of(String name) {
        return Arrays.stream(values())
                .filter(seg -> seg.getExchange().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
    }
}
