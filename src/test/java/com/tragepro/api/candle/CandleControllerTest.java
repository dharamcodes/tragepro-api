package com.tragepro.api.candle;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.repository.CandleRepository;
import com.tragepro.api.common.ApiTestSetup;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class CandleControllerTest extends ApiTestSetup {

    private static final String SYMBOL_ID = "BTCUSD";
    private static final String SYMBOL_NAME = "Bitcoin";
    private static final long TIMESTAMP = 1609459200000L;
    private static final String BASE_URL = "/config/v1/candle";

    @Autowired
    private CandleRepository candleRepository;

    private CandleRequest candleRequest;
    private String savedId;

    @BeforeEach
    void setUp() {
        candleRepository.deleteAll();
        candleRequest = new CandleRequest(
                new Symbol(SYMBOL_ID, SYMBOL_NAME), new Candle(TIMESTAMP, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        CandleEntity entity = new CandleEntity();
        entity.setSymbol(candleRequest.getSymbol());
        entity.setCandle(candleRequest.getCandle());
        savedId = candleRepository.save(entity).getId();
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    @Test
    void testCreate_Success() throws Exception {
        CandleRequest newRequest = new CandleRequest(
                new Symbol("ETHUSD", "Ethereum"), new Candle(TIMESTAMP + 1000, 1800.0, 1900.0, 1750.0, 1850.0, 500.0));

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol.id").value("ETHUSD"))
                .andExpect(jsonPath("$.candle.open").value(1800.0));
    }

    @Test
    void testCreate_ValidationFails_WhenBodyEmpty() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CandleRequest())))
                .andExpect(status().is4xxClientError());
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────

    @Test
    void testGetById_Success() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol.id").value(SYMBOL_ID))
                .andExpect(jsonPath("$.candle.close").value(29300.0));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", "nonExistentId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    // ─── GET ALL ──────────────────────────────────────────────────────────────

    @Test
    void testGetAll_Success() throws Exception {
        System.out.println("RAW DOC: "
                + org.springframework.data.mongodb.core.MongoTemplate.class
                        .cast(org.springframework.test.util.ReflectionTestUtils.getField(
                                candleRepository, "mongoOperations"))
                        .findAll(org.bson.Document.class, "candle"));
        mockMvc.perform(get(BASE_URL).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].symbolId").value(SYMBOL_ID));
    }

    @Test
    void testGetAll_EmptyCollection_ReturnsEmptyPage() throws Exception {
        candleRepository.deleteAll();
        mockMvc.perform(get(BASE_URL).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // ─── GET BY SYMBOL ────────────────────────────────────────────────────────

    @Test
    void testGetBySymbol_Success() throws Exception {
        mockMvc.perform(get(BASE_URL + "/symbol/{symbolId}", SYMBOL_ID).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].symbolId").value(SYMBOL_ID));
    }

    @Test
    void testGetBySymbol_EmptyForUnknownSymbol() throws Exception {
        mockMvc.perform(get(BASE_URL + "/symbol/{symbolId}", "UNKNOWN").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // ─── GET BY SYMBOL + TIME RANGE ───────────────────────────────────────────

    @Test
    void testGetBySymbolAndTimeRange_Success() throws Exception {
        long from = TIMESTAMP - 1000;
        long to = TIMESTAMP + 1000;

        mockMvc.perform(get(BASE_URL + "/symbol/{symbolId}/range", SYMBOL_ID)
                        .header("Authorization", authToken)
                        .param("from", String.valueOf(from))
                        .param("to", String.valueOf(to)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].symbolId").value(SYMBOL_ID))
                .andExpect(jsonPath("$[0].timestamp").value(TIMESTAMP));
    }

    @Test
    void testGetBySymbolAndTimeRange_EmptyWhenOutOfRange() throws Exception {
        mockMvc.perform(get(BASE_URL + "/symbol/{symbolId}/range", SYMBOL_ID)
                        .header("Authorization", authToken)
                        .param("from", "1000000000")
                        .param("to", "1000000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ─── GET LATEST PER SYMBOL ────────────────────────────────────────────────

    @Test
    void testGetLatestPerSymbol_Success() throws Exception {
        mockMvc.perform(get(BASE_URL + "/symbols/latest").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].symbolId").value(SYMBOL_ID));
    }

    @Test
    void testGetLatestPerSymbol_EmptyCollection() throws Exception {
        candleRepository.deleteAll();
        mockMvc.perform(get(BASE_URL + "/symbols/latest").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ─── MANUAL BULK INGEST ───────────────────────────────────────────────────

    @Test
    void testManualBulkIngest_Success() throws Exception {
        List<CandleRequest> payload = List.of(
                new CandleRequest(
                        new Symbol("AAPL", "Apple Inc"),
                        new Candle(TIMESTAMP + 5000, 175.0, 180.0, 172.0, 178.5, 25000.0)),
                new CandleRequest(
                        new Symbol("GOOGL", "Alphabet Inc"),
                        new Candle(TIMESTAMP + 5000, 140.0, 145.0, 138.0, 143.2, 18000.0)));

        mockMvc.perform(post(BASE_URL + "/ingest")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void testManualBulkIngest_UpsertDeduplicates() throws Exception {
        List<CandleRequest> same = List.of(candleRequest);

        mockMvc.perform(post(BASE_URL + "/ingest")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(same)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/symbol/{symbolId}", SYMBOL_ID).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Test
    void testUpdate_Success() throws Exception {
        CandleRequest updateRequest = new CandleRequest(
                new Symbol(SYMBOL_ID, "Updated Bitcoin"),
                new Candle(TIMESTAMP, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        mockMvc.perform(put(BASE_URL + "/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol.name").value("Updated Bitcoin"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        CandleRequest updateRequest = new CandleRequest(
                new Symbol(SYMBOL_ID, "Updated Bitcoin"),
                new Candle(TIMESTAMP, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        mockMvc.perform(put(BASE_URL + "/{id}", "unknownId")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test
    void testDelete_Success() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", "unknownId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    // ─── AUTH GUARD ───────────────────────────────────────────────────────────

    @Test
    void testAllEndpoints_RequireAuth() throws Exception {
        mockMvc.perform(get(BASE_URL)).andExpect(status().is4xxClientError());
        mockMvc.perform(get(BASE_URL + "/" + savedId)).andExpect(status().is4xxClientError());
        mockMvc.perform(get(BASE_URL + "/symbol/" + SYMBOL_ID)).andExpect(status().is4xxClientError());
        mockMvc.perform(get(BASE_URL + "/symbols/latest")).andExpect(status().is4xxClientError());
    }
}
