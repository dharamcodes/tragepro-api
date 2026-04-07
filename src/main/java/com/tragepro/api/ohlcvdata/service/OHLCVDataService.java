package com.tragepro.api.ohlcvdata.service;

import com.tragepro.api.ohlcvdata.model.request.OHLCVDataRequest;
import com.tragepro.api.ohlcvdata.model.response.OHLCVDataResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OHLCVDataService {

    OHLCVDataResponse create(OHLCVDataRequest ohlcvDataRequest);

    Optional<OHLCVDataResponse> getById(String id);

    Page<OHLCVDataResponse> getAll(Pageable pageable);

    OHLCVDataResponse update(String id, OHLCVDataRequest ohlcvDataRequest);

    void delete(String id);
}
