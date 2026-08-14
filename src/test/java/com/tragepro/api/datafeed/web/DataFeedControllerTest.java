package com.tragepro.api.datafeed.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tragepro.api.core.ApiTestSetup;
import com.tragepro.api.datafeed.WatchListService;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.request.WatchListRequest;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class DataFeedControllerTest extends ApiTestSetup {

  @Autowired private WatchListService watchListService;

  @BeforeEach
  void setUp() {
    try {
      WatchListRequest wlRequest =
          WatchListRequest.builder()
              .name("WL_TEST")
              .description("Test Watchlist")
              .stocks(Set.of(new SymbolDataModel("AAPL", "Apple Inc.")))
              .build();
      watchListService.create(wlRequest);
    } catch (Exception e) {
      // Ignored if already created
    }
  }

  @Test
  void testLoadData_Success() throws Exception {
    LoadCandleRequest loadReq = new LoadCandleRequest("WL_TEST", 1);

    mockMvc
        .perform(
            post("/api/v1/datafeed/load")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.watchList").value("WL_TEST"))
        .andExpect(jsonPath("$.message").value("Data load initiated successfully"));
  }

  @Test
  void testLoadData_NotFound() throws Exception {
    LoadCandleRequest loadReq = new LoadCandleRequest("WL_NON_EXISTENT", 1);

    mockMvc
        .perform(
            post("/api/v1/datafeed/load")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadReq)))
        .andExpect(status().isNotFound());
  }
}
