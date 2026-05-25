package com.tragepro.api.candle.provider;

import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.request.CandleRequest;
import java.util.List;

public interface DataProviderClient {

    List<CandleRequest> fetchAll(CandleInterval interval);
}
