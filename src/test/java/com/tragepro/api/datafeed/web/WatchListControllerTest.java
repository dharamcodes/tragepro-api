package com.tragepro.api.datafeed.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.datafeed.core.repository.WatchListRepository;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.Exchange;
import com.tragepro.api.domain.datafeed.entity.WatchListEntity;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class WatchListControllerTest extends ApiTestSetup {

    @Autowired
    private WatchListRepository watchListRepository;

    private WatchListRequest watchListRequest;
    private String savedId;

    @BeforeEach
    void setUp() {
        watchListRepository.deleteAll();

        Set<SymbolDataModel> stocks = new HashSet<>();
        stocks.add(new SymbolDataModel("AAPL", "Apple Inc."));
        stocks.add(new SymbolDataModel("MSFT", "Microsoft Corp."));

        watchListRequest = new WatchListRequest("My Watchlist", "Tech stocks", Exchange.NSE, stocks);

        WatchListEntity entity = new WatchListEntity();
        entity.setName(watchListRequest.name());
        entity.setDescription(watchListRequest.description());
        entity.setStocks(watchListRequest.stocks());

        WatchListEntity savedEntity = watchListRepository.save(entity);
        savedId = savedEntity.getId();
    }

    @Test
    void testCreate_Success() throws Exception {
        mockMvc.perform(post("/api/v1/watchlists")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(watchListRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Watchlist"))
                .andExpect(jsonPath("$.description").value("Tech stocks"))
                .andExpect(jsonPath("$.stocks").isArray())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void testGetById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/watchlists/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.name").value("My Watchlist"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/watchlists/{id}", "nonExistentId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAll_Success() throws Exception {
        mockMvc.perform(get("/api/v1/watchlists").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("My Watchlist"));
    }

    @Test
    void testGetAll_NotFound() throws Exception {
        watchListRepository.deleteAll();
        mockMvc.perform(get("/api/v1/watchlists").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_Success() throws Exception {
        Set<SymbolDataModel> updatedStocks = new HashSet<>();
        updatedStocks.add(new SymbolDataModel("TSLA", "Tesla Inc."));
        WatchListRequest updateRequest =
                new WatchListRequest("Updated Watchlist", "EV stocks", Exchange.NSE, updatedStocks);

        mockMvc.perform(put("/api/v1/watchlists/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Watchlist"))
                .andExpect(jsonPath("$.description").value("EV stocks"))
                .andExpect(jsonPath("$.stocks[0].symbol").value("TSLA"));
    }

    @Test
    void testPatch_Rename_Success() throws Exception {
        WatchListRequest patchRequest =
                WatchListRequest.builder().name("New List Name").build();

        mockMvc.perform(patch("/api/v1/watchlists/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New List Name"))
                .andExpect(jsonPath("$.description").value("Tech stocks")); // unchanged
    }

    @Test
    void testPatch_UpdateDescription_Success() throws Exception {
        WatchListRequest patchRequest =
                WatchListRequest.builder().description("New Description").build();

        mockMvc.perform(patch("/api/v1/watchlists/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Watchlist")) // unchanged
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    void testPatch_AddStock_Success() throws Exception {
        SymbolDataModel newStock = new SymbolDataModel("GOOGL", "Alphabet Inc.");
        Set<SymbolDataModel> stocks = new HashSet<>(watchListRequest.stocks());
        stocks.add(newStock);
        WatchListRequest patchRequest =
                WatchListRequest.builder().stocks(stocks).build();

        mockMvc.perform(patch("/api/v1/watchlists/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks.length()").value(3));
    }

    @Test
    void testDelete_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/watchlists/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/watchlists/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        WatchListRequest updateRequest =
                WatchListRequest.builder().name("Non-existent").build();
        mockMvc.perform(put("/api/v1/watchlists/{id}", "nonExistentId")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/watchlists/{id}", "nonExistentId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatch_NotFound() throws Exception {
        WatchListRequest patchRequest =
                WatchListRequest.builder().name("Non-existent").build();
        mockMvc.perform(patch("/api/v1/watchlists/{id}", "nonExistentId")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isNotFound());
    }
}
