package com.tragepro.api.datafeed.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.core.ApiTestSetup;
import com.tragepro.api.datafeed.model.CandleDataModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.datafeed.model.entity.CandleEntity;
import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.repository.CandleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class CandleControllerTest extends ApiTestSetup {

  @Autowired private CandleRepository candleRepository;

  private CandleRequest candleRequest;
  private String savedId;

  @BeforeEach
  void setUp() {
    candleRequest =
        new CandleRequest(
            new SymbolDataModel("BTCUSD", "Bitcoin"),
            new CandleDataModel(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000L));

    CandleEntity entity = new CandleEntity();
    entity.setSymbolData(candleRequest.symbolData());
    entity.setCandleData(candleRequest.candleData());

    CandleEntity savedEntity = candleRepository.save(entity);
    savedId = savedEntity.getId();
  }

  @Test
  void testCreate_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/candles")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(candleRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.symbolData.symbol").value("BTCUSD"))
        .andExpect(jsonPath("$.candleData.open").value(29000.0));
  }

  @Test
  void testCreate_Exception() throws Exception {
    var updatedRequest = CandleRequest.builder().build();
    mockMvc
        .perform(
            post("/api/v1/candles")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void testGetById_Exception() throws Exception {
    mockMvc
        .perform(get("/api/v1/candles/{id}", "nonExistentId").header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetById_Success() throws Exception {
    mockMvc
        .perform(get("/api/v1/candles/{id}", savedId).header("Authorization", authToken))
        .andExpect(status().isOk());
  }

  @Test
  void testGetAll_Success() throws Exception {
    mockMvc
        .perform(get("/api/v1/candles").header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].symbolData.symbol").value("BTCUSD"));
  }

  @Test
  void testGetAll_Exception() throws Exception {
    candleRepository.deleteAll();
    mockMvc
        .perform(get("/api/v1/candles").header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdate_Success() throws Exception {
    CandleRequest updateRequest =
        new CandleRequest(
            new SymbolDataModel("BTCUSD", "Updated Bitcoin"),
            new CandleDataModel(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000L));

    mockMvc
        .perform(
            put("/api/v1/candles/{id}", savedId)
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.symbolData.name").value("Updated Bitcoin"));
  }

  @Test
  void testUpdate_Exception() throws Exception {
    CandleRequest updateRequest =
        new CandleRequest(
            new SymbolDataModel("BTCUSD", "Updated Bitcoin"),
            new CandleDataModel(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000L));

    mockMvc
        .perform(
            put("/api/v1/candles/{id}", "testDummyId")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDelete_Success() throws Exception {
    mockMvc
        .perform(delete("/api/v1/candles/{id}", savedId).header("Authorization", authToken))
        .andExpect(status().isOk());
  }

  @Test
  void testDelete_Exception() throws Exception {
    mockMvc
        .perform(delete("/api/v1/candles/{id}", "testDummyId").header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetLatestCandlesBySymbols_Success() throws Exception {
    java.util.Set<String> symbols = java.util.Set.of("BTCUSD", "ETHUSD");
    mockMvc
        .perform(
            post("/api/v1/candles/latest")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(symbols)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].symbolData.symbol").value("BTCUSD"));
  }
}
