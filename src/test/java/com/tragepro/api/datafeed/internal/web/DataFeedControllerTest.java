package com.tragepro.api.datafeed.internal.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.dto.LoadCandleRequest;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.internal.entity.SecurityEntity;
import com.tragepro.api.datafeed.internal.repository.SecurityRepository;
import com.tragepro.api.datafeed.internal.service.DatafeedService;
import com.tragepro.api.datafeed.internal.service.WatchListService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class DataFeedControllerTest extends ApiTestSetup {

  @Autowired private WatchListService watchListService;
  @Autowired private SecurityRepository securityRepository;
  @Autowired private DatafeedService datafeedService;

  @BeforeEach
  void setUp() {
    securityRepository.deleteAll();
    SecurityEntity sec = new SecurityEntity();
    sec.setSymbol("AAPL");
    sec.setName("Apple Inc.");
    sec.setSecurityId(101);
    securityRepository.save(sec);

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

    // Wait briefly for async data load thread to complete execution
    Thread.sleep(1500);
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

  @Test
  void testLoadData_EmptyWatchlist() {
    WatchListRequest emptyWl =
        WatchListRequest.builder().name("WL_EMPTY").description("Empty").stocks(Set.of()).build();
    watchListService.create(emptyWl);

    LoadCandleRequest loadReq = new LoadCandleRequest("WL_EMPTY", 1);
    var response = datafeedService.loadData(loadReq);
    org.junit.jupiter.api.Assertions.assertEquals(
        "No symbols found in watchlist to process", response.message());
  }

  @Test
  void testWebSocketRequest_RecordAccessors() {
    com.tragepro.api.datafeed.dto.WebSocketRequest wsReq =
        com.tragepro.api.datafeed.dto.WebSocketRequest.builder()
            .id("ws1")
            .action("SUBSCRIBE")
            .build();
    org.junit.jupiter.api.Assertions.assertEquals("ws1", wsReq.id());
    org.junit.jupiter.api.Assertions.assertEquals("SUBSCRIBE", wsReq.action());
  }
}
