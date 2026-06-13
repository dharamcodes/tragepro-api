package com.tragepro.api.journal.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String value;
}
