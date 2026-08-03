package com.tragepro.api.strategy.internal.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.strategy.dto.StatusModel;
import com.tragepro.api.strategy.dto.StrategyModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.dto.WorkflowRequest;
import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.constant.StrategyStep;
import com.tragepro.api.strategy.internal.repository.StrategyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class StrategyControllerTest extends ApiTestSetup {

  @Autowired private StrategyRepository strategyRepository;

  private StrategyRequest strategyRequest;

  @BeforeEach
  void setUp() {
    strategyRepository.deleteAll();

    strategyRequest =
        StrategyRequest.builder()
            .strategy(
                StrategyModel.builder()
                    .name("Swing Strategy")
                    .desc("Swing Trading")
                    .watchlist("WL_TECH")
                    .build())
            .symbolData(
                SymbolModel.builder()
                    .symbol("AAPL")
                    .name("Apple Inc.")
                    .exchange(Exchange.NSE)
                    .build())
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.INITIALIZING)
                    .step(StrategyStep.INIT)
                    .build())
            .build();
  }

  @Test
  void testCreateStrategy_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/strategy")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategyRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.strategy.name").value("Swing Strategy"))
        .andExpect(jsonPath("$.symbolData.symbol").value("AAPL"));
  }

  @Test
  void testCreateStrategy_ValidationFailure() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/strategy")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllStrategies_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/strategy")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategyRequest)))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/v1/strategy").header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void testGetAllStrategies_Empty() throws Exception {
    mockMvc
        .perform(get("/api/v1/strategy").header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void testGetById_SuccessAndNotFound() throws Exception {
    String responseJson =
        mockMvc
            .perform(
                post("/api/v1/strategy")
                    .header("Authorization", authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(strategyRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var savedEntity = strategyRepository.findAll().get(0);
    String id = savedEntity.getId();

    mockMvc
        .perform(get("/api/v1/strategy/{id}", id).header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.strategy.name").value("Swing Strategy"));

    mockMvc
        .perform(get("/api/v1/strategy/{id}", "nonExistentId").header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateStrategy_SuccessAndNotFound() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/strategy")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategyRequest)))
        .andExpect(status().isOk());

    var savedEntity = strategyRepository.findAll().get(0);
    String id = savedEntity.getId();

    StrategyRequest updateRequest =
        StrategyRequest.builder()
            .strategy(
                StrategyModel.builder()
                    .name("Updated Strategy")
                    .desc("Updated Desc")
                    .watchlist("WL_TECH")
                    .build())
            .symbolData(
                SymbolModel.builder()
                    .symbol("AAPL")
                    .name("Apple Inc.")
                    .exchange(Exchange.NSE)
                    .build())
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.BUILDING)
                    .step(StrategyStep.BUILD)
                    .build())
            .build();

    mockMvc
        .perform(
            put("/api/v1/strategy/{id}", id)
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.strategy.name").value("Updated Strategy"));

    mockMvc
        .perform(
            put("/api/v1/strategy/{id}", "nonExistentId")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteStrategy_SuccessAndNotFound() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/strategy")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategyRequest)))
        .andExpect(status().isOk());

    var savedEntity = strategyRepository.findAll().get(0);
    String id = savedEntity.getId();

    mockMvc
        .perform(delete("/api/v1/strategy/{id}", id).header("Authorization", authToken))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(delete("/api/v1/strategy/{id}", id).header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testRunStrategy_SuccessAndBadRequest() throws Exception {
    WorkflowRequest runReq = WorkflowRequest.builder().strategyId("strat1").symbol("AAPL").build();

    mockMvc
        .perform(
            post("/api/v1/strategy/run")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(runReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("SUCCESS"));

    mockMvc
        .perform(
            post("/api/v1/strategy/run")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest());
  }
}
