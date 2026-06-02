package com.tragepro.api.data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.data.model.SymbolData;
import com.tragepro.api.data.model.entity.WatchListEntity;
import com.tragepro.api.data.model.request.WatchListRequest;
import com.tragepro.api.data.repository.WatchListRepository;
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

        Set<SymbolData> stocks = new HashSet<>();
        stocks.add(new SymbolData("AAPL", "Apple Inc."));
        stocks.add(new SymbolData("MSFT", "Microsoft Corp."));

        watchListRequest = new WatchListRequest("My Watchlist", "Tech stocks", stocks);

        WatchListEntity entity = new WatchListEntity();
        entity.setName(watchListRequest.getName());
        entity.setDescription(watchListRequest.getDescription());
        entity.setStocks(watchListRequest.getStocks());

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
        Set<SymbolData> updatedStocks = new HashSet<>();
        updatedStocks.add(new SymbolData("TSLA", "Tesla Inc."));
        WatchListRequest updateRequest = new WatchListRequest("Updated Watchlist", "EV stocks", updatedStocks);

        mockMvc.perform(put("/api/v1/watchlists/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Watchlist"))
                .andExpect(jsonPath("$.description").value("EV stocks"))
                .andExpect(jsonPath("$.stocks[0].id").value("TSLA"));
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
        SymbolData newStock = new SymbolData("GOOGL", "Alphabet Inc.");
        Set<SymbolData> stocks = new HashSet<>(watchListRequest.getStocks());
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
}
