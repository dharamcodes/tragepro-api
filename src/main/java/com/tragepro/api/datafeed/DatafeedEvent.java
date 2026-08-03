package com.tragepro.api.datafeed;

import com.tragepro.api.datafeed.dto.CandleResponse;
import java.util.List;

public record DatafeedEvent(String eventId, List<CandleResponse> candleResponse) {}
