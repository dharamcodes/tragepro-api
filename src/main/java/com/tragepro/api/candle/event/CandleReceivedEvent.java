package com.tragepro.api.candle.event;

import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;

public record CandleReceivedEvent(Symbol symbol, Candle candle) {}
