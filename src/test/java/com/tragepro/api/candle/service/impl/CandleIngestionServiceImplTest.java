package com.tragepro.api.candle.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.provider.DataProviderClient;
import com.tragepro.api.candle.repository.CandleRepository;
import com.tragepro.api.common.ApiTestSetup;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CandleIngestionServiceImplTest extends ApiTestSetup {

    @Autowired
    private CandleIngestionServiceImpl ingestionService;

    @Autowired
    private CandleRepository candleRepository;

    @MockitoBean
    private DataProviderClient dataProviderClient;

    @BeforeEach
    void setUp() {
        candleRepository.deleteAll();
    }

    @Test
    void testIngestAll_Success() {
        // Arrange
        CandleRequest req1 = new CandleRequest(
                new Symbol("BTCUSD", "Bitcoin"), new Candle(1600000000L, 20000.0, 21000.0, 19000.0, 20500.0, 100.0));
        CandleRequest req2 = new CandleRequest(
                new Symbol("ETHUSD", "Ethereum"), new Candle(1600000000L, 1500.0, 1600.0, 1400.0, 1550.0, 500.0));
        when(dataProviderClient.fetchAll(any())).thenReturn(List.of(req1, req2));

        // Act
        ingestionService.ingestAll(CandleInterval.ONE_MINUTE);

        // Assert
        assertEquals(2, candleRepository.count());
        List<CandleEntity> entities = candleRepository.findAll();
        assertEquals("BTCUSD", entities.get(0).getSymbol().id());
        assertEquals("ETHUSD", entities.get(1).getSymbol().id());
    }

    @Test
    void testIngestAll_EmptyData() {
        // Arrange
        when(dataProviderClient.fetchAll(any())).thenReturn(List.of());

        // Act
        ingestionService.ingestAll(CandleInterval.ONE_MINUTE);

        // Assert
        assertEquals(0, candleRepository.count());
    }

    @Test
    void testBulkUpsert_UpdateExisting() {
        // Arrange
        CandleRequest req1 = new CandleRequest(
                new Symbol("BTCUSD", "Bitcoin"), new Candle(1600000000L, 20000.0, 21000.0, 19000.0, 20500.0, 100.0));
        ingestionService.bulkUpsert(List.of(req1));
        assertEquals(1, candleRepository.count());

        // Act: Update same record, different values
        CandleRequest req1Updated = new CandleRequest(
                new Symbol("BTCUSD", "Bitcoin"), new Candle(1600000000L, 21000.0, 22000.0, 19000.0, 21500.0, 200.0));
        CandleRequest req2 = new CandleRequest(
                new Symbol("ETHUSD", "Ethereum"), new Candle(1600000000L, 1500.0, 1600.0, 1400.0, 1550.0, 500.0));

        int upsertCount = ingestionService.bulkUpsert(List.of(req1Updated, req2));

        // Assert
        assertEquals(2, upsertCount); // 1 updated, 1 inserted
        assertEquals(2, candleRepository.count());

        CandleEntity updatedBtc = candleRepository.findAll().stream()
                .filter(e -> e.getSymbol().id().equals("BTCUSD"))
                .findFirst()
                .orElseThrow();
        assertEquals(21500.0, updatedBtc.getCandle().close());
    }

    @Test
    void testBulkUpsert_EmptyList() {
        int upsertCount = ingestionService.bulkUpsert(List.of());
        assertEquals(0, upsertCount);
        assertEquals(0, candleRepository.count());
    }
}
