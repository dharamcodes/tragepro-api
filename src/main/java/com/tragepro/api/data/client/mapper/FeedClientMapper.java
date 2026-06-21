package com.tragepro.api.data.client.mapper;

import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.FeedClientResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedClientMapper {

  default List<CandleRequest> map(FeedClientResponse response, FeedClientRequest request) {
    if (response == null
        || request == null
        || response.timestamp() == null
        || response.timestamp().isEmpty()) {
      return Collections.emptyList();
    }

    SymbolData symbolData = buildSymbolData(request);

    return IntStream.range(0, response.timestamp().size())
        .mapToObj(index -> buildCandleRequest(response, symbolData, index))
        .toList();
  }

  private SymbolData buildSymbolData(FeedClientRequest request) {
    return SymbolData.builder().name(request.instrument()).build();
  }

  private CandleRequest buildCandleRequest(
      FeedClientResponse response, SymbolData symbolData, int index) {
    CandleData candleData =
        CandleData.builder()
            .timestamp(response.timestamp().get(index))
            .open(getValue(response.open(), index, 0.0))
            .high(getValue(response.high(), index, 0.0))
            .low(getValue(response.low(), index, 0.0))
            .close(getValue(response.close(), index, 0.0))
            .volume(getValue(response.volume(), index, 0L))
            .build();

    return CandleRequest.builder().symbolData(symbolData).candleData(candleData).build();
  }

  private <T> T getValue(List<T> list, int index, T defaultValue) {
    return (list != null && list.size() > index && list.get(index) != null)
        ? list.get(index)
        : defaultValue;
  }
}
