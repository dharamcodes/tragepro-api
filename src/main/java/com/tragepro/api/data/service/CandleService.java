package com.tragepro.api.data.service;

import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.response.CandleResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandleService {

  CandleResponse create(CandleRequest candleRequest);

  Optional<CandleResponse> getById(String id);

  Page<CandleResponse> getAll(Pageable pageable);

  CandleResponse update(String id, CandleRequest candleRequest);

  void delete(String id);

  boolean isCandleExists(String name, long timestamp);
}
