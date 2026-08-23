package com.tragepro.api.datafeed.service.mapper;

import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.response.FeedClientResponse;
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

        SymbolDataModel symbolDataModel = buildSymbolData(request);

        return IntStream.range(0, response.timestamp().size())
                .mapToObj(index -> buildCandleRequest(response, symbolDataModel, index))
                .toList();
    }

    private SymbolDataModel buildSymbolData(FeedClientRequest request) {
        return SymbolDataModel.builder().name(request.instrument()).build();
    }

    private CandleRequest buildCandleRequest(FeedClientResponse response, SymbolDataModel symbolDataModel, int index) {
        CandleDataModel candleDataModel = CandleDataModel.builder()
                .timestamp(response.timestamp().get(index))
                .open(getValue(response.open(), index, 0.0))
                .high(getValue(response.high(), index, 0.0))
                .low(getValue(response.low(), index, 0.0))
                .close(getValue(response.close(), index, 0.0))
                .volume(getValue(response.volume(), index, 0L))
                .build();

        return CandleRequest.builder()
                .symbolData(symbolDataModel)
                .candleData(candleDataModel)
                .build();
    }

    private <T> T getValue(List<T> list, int index, T defaultValue) {
        return (list != null && list.size() > index && list.get(index) != null) ? list.get(index) : defaultValue;
    }
}
