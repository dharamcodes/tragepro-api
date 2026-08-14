package com.tragepro.api.datafeed.event;

import com.tragepro.api.datafeed.model.response.CandleResponse;
import java.util.List;

public record DataEvent(String eventId, List<CandleResponse> candleResponse) {}
