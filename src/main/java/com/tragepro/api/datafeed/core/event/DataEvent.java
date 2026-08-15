package com.tragepro.api.datafeed.core.event;

import com.tragepro.api.domain.datafeed.response.CandleResponse;
import java.util.List;

public record DataEvent(String eventId, List<CandleResponse> candleResponse) {}
