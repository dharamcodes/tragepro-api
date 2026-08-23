package com.tragepro.api.domain.journal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeType {
    LONG("Long"),
    SHORT("Short");

    private final String value;
}
