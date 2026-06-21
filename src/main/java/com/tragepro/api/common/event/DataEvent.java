package com.tragepro.api.common.event;

import com.tragepro.api.common.model.response.CandleResponse;
import java.util.List;

public record DataEvent(String eventId, List<CandleResponse> candleResponse) {}
