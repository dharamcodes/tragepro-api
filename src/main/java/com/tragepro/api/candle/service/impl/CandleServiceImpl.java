package com.tragepro.api.candle.service.impl;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.model.response.CandleResponse;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.candle.repository.CandleRepository;
import com.tragepro.api.candle.service.CandleService;
import com.tragepro.api.candle.service.mapper.CandleMapper;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleServiceImpl implements CandleService {

    private static final int MAX_RANGE_LIMIT = 10_000;
    private static final String COLLECTION = "candle";
    private static final String TIMESTAMP_FIELD = "candle.timestamp";
    private static final String SYMBOL_ID_FIELD = "symbol.id";

    private final CandleRepository candleRepository;
    private final MapperFactory<CandleMapper> mapperFactory;
    private final MongoTemplate mongoTemplate;

    @Override
    public CandleResponse create(CandleRequest candleRequest) {
        var mapper = mapperFactory.getMapper(MapperType.CANDLE_MAPPER);
        var candleEntity = mapper.requestToEntity(candleRequest);
        candleRepository.save(candleEntity);
        return mapper.entityToResponse(candleEntity);
    }

    @Override
    public CandleResponse getById(String id) {
        var mapper = mapperFactory.getMapper(MapperType.CANDLE_MAPPER);
        return candleRepository.findById(id).map(mapper::entityToResponse).orElseThrow(() -> {
            log.error("No candle record found for id: {}", id);
            return new AppException(ErrorType.DATA_NOT_FOUND);
        });
    }

    @Override
    public Page<CandleSummaryResponse> getAll(Pageable pageable) {
        return candleRepository.findAll(pageable).map(this::toSummary);
    }

    @Override
    public Page<CandleSummaryResponse> getBySymbol(String symbolId, Pageable pageable) {
        return candleRepository.findBySymbolId(symbolId, pageable).map(this::toSummary);
    }

    @Override
    public List<CandleSummaryResponse> getBySymbolAndTimeRange(String symbolId, long fromTimestamp, long toTimestamp) {
        return candleRepository
                .findBySymbolIdAndCandleTimestampBetweenOrderByCandleTimestampAsc(symbolId, fromTimestamp, toTimestamp)
                .stream()
                .limit(MAX_RANGE_LIMIT)
                .map(this::toSummary)
                .toList();
    }

    @Override
    public List<CandleSummaryResponse> getLatestPerSymbol() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.sort(Sort.by(DESC, TIMESTAMP_FIELD)),
                Aggregation.group(SYMBOL_ID_FIELD)
                        .first("_id")
                        .as("id")
                        .first("symbol.name")
                        .as("symbolName")
                        .first("candle.timestamp")
                        .as("timestamp")
                        .first("candle.open")
                        .as("open")
                        .first("candle.high")
                        .as("high")
                        .first("candle.low")
                        .as("low")
                        .first("candle.close")
                        .as("close")
                        .first("candle.volume")
                        .as("volume"));

        AggregationResults<org.bson.Document> results =
                mongoTemplate.aggregate(agg, COLLECTION, org.bson.Document.class);
        return results.getMappedResults().stream()
                .map(doc -> {
                    log.info("AGGREGATION DOC: {}", doc.toJson());
                    return CandleSummaryResponse.builder()
                            .symbolId(doc.getString("_id"))
                            .id(doc.get("id") != null ? doc.get("id").toString() : null)
                            .symbolName(doc.getString("symbolName"))
                            .timestamp(doc.getLong("timestamp"))
                            .open(doc.getDouble("open"))
                            .high(doc.getDouble("high"))
                            .low(doc.getDouble("low"))
                            .close(doc.getDouble("close"))
                            .volume(doc.getDouble("volume"))
                            .build();
                })
                .toList();
    }

    @Override
    public List<CandleSummaryResponse> getLatestForSymbols(List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return List.of();
        }

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(org.springframework.data.mongodb.core.query.Criteria.where(SYMBOL_ID_FIELD)
                        .in(symbols)),
                Aggregation.sort(Sort.by(DESC, TIMESTAMP_FIELD)),
                Aggregation.group(SYMBOL_ID_FIELD)
                        .first("_id")
                        .as("id")
                        .first("symbol.name")
                        .as("symbolName")
                        .first("candle.timestamp")
                        .as("timestamp")
                        .first("candle.open")
                        .as("open")
                        .first("candle.high")
                        .as("high")
                        .first("candle.low")
                        .as("low")
                        .first("candle.close")
                        .as("close")
                        .first("candle.volume")
                        .as("volume"));

        AggregationResults<org.bson.Document> results =
                mongoTemplate.aggregate(agg, COLLECTION, org.bson.Document.class);
        return results.getMappedResults().stream()
                .map(doc -> CandleSummaryResponse.builder()
                        .symbolId(doc.getString("_id"))
                        .id(doc.get("id") != null ? doc.get("id").toString() : null)
                        .symbolName(doc.getString("symbolName"))
                        .timestamp(doc.getLong("timestamp"))
                        .open(doc.getDouble("open"))
                        .high(doc.getDouble("high"))
                        .low(doc.getDouble("low"))
                        .close(doc.getDouble("close"))
                        .volume(doc.getDouble("volume"))
                        .build())
                .toList();
    }

    @Override
    public CandleResponse update(String id, CandleRequest candleRequest) {
        var mapper = mapperFactory.getMapper(MapperType.CANDLE_MAPPER);
        var candleEntity = candleRepository.findById(id).orElseThrow(() -> {
            log.error("No candle record found for update, id: {}", id);
            return new AppException(ErrorType.DATA_NOT_FOUND);
        });
        mapper.merge(candleRequest, candleEntity);
        candleRepository.save(candleEntity);
        return mapper.entityToResponse(candleEntity);
    }

    @Override
    public void delete(String id) {
        var entityToDelete = candleRepository.findById(id).orElseThrow(() -> {
            log.error("No candle record found for delete, id: {}", id);
            return new AppException(ErrorType.DATA_NOT_FOUND);
        });
        candleRepository.delete(entityToDelete);
    }

    private CandleSummaryResponse toSummary(CandleEntity entity) {
        return CandleSummaryResponse.builder()
                .id(entity.getId())
                .symbolId(entity.getSymbol().id())
                .symbolName(entity.getSymbol().name())
                .timestamp(entity.getCandle().timestamp())
                .open(entity.getCandle().open())
                .high(entity.getCandle().high())
                .low(entity.getCandle().low())
                .close(entity.getCandle().close())
                .volume(entity.getCandle().volume())
                .build();
    }
}
