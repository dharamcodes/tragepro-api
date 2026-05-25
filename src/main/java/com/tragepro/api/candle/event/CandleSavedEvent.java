package com.tragepro.api.candle.event;

import com.tragepro.api.candle.model.response.CandleSummaryResponse;

public record CandleSavedEvent(CandleSummaryResponse candleSummaryResponse) {}
