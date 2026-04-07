package com.tragepro.api.ohlcvdata;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.ohlcvdata.model.OHLCVData;
import com.tragepro.api.ohlcvdata.model.SymbolData;
import com.tragepro.api.ohlcvdata.model.entity.OHLCVDataEntity;
import com.tragepro.api.ohlcvdata.model.request.OHLCVDataRequest;
import com.tragepro.api.ohlcvdata.repository.OHLCVDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class OHLCVDataControllerTest extends ApiTestSetup {

    @Autowired
    private OHLCVDataRepository ohlcvDataRepository;

    private OHLCVDataRequest ohlcvDataRequest;
    private String savedId;

    @BeforeEach
    void setUp() {
        ohlcvDataRequest = new OHLCVDataRequest(
                new SymbolData("BTCUSD", "Bitcoin"),
                new OHLCVData(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        OHLCVDataEntity entity = new OHLCVDataEntity();
        entity.setSymbolData(ohlcvDataRequest.getSymbolData());
        entity.setOhlcvData(ohlcvDataRequest.getOhlcvData());

        OHLCVDataEntity savedEntity = ohlcvDataRepository.save(entity);
        savedId = savedEntity.getId();
    }

    @Test
    void testCreate_Success() throws Exception {
        mockMvc.perform(post("/api/v1/ohlcvdata")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ohlcvDataRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbolData.id").value("BTCUSD"))
                .andExpect(jsonPath("$.ohlcvData.open").value(29000.0));
    }

    @Test
    void testCreate_Exception() throws Exception {
        var updatedRequest = new OHLCVDataRequest();
        mockMvc.perform(post("/api/v1/ohlcvdata")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetById_Exception() throws Exception {
        mockMvc.perform(get("/api/v1/ohlcvdata/{id}", "nonExistentId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/ohlcvdata/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll_Success() throws Exception {
        mockMvc.perform(get("/api/v1/ohlcvdata").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].symbolData.id").value("BTCUSD"));
    }

    @Test
    void testGetAll_Exception() throws Exception {
        ohlcvDataRepository.deleteAll();
        mockMvc.perform(get("/api/v1/ohlcvdata").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_Success() throws Exception {
        OHLCVDataRequest updateRequest = new OHLCVDataRequest(
                new SymbolData("BTCUSD", "Updated Bitcoin"),
                new OHLCVData(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        mockMvc.perform(put("/api/v1/ohlcvdata/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbolData.name").value("Updated Bitcoin"));
    }

    @Test
    void testUpdate_Exception() throws Exception {
        OHLCVDataRequest updateRequest = new OHLCVDataRequest(
                new SymbolData("BTCUSD", "Updated Bitcoin"),
                new OHLCVData(1609459200000L, 29000.0, 29500.0, 28500.0, 29300.0, 1000.0));

        mockMvc.perform(put("/api/v1/ohlcvdata/{id}", "testDummyId")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/ohlcvdata/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_Exception() throws Exception {
        mockMvc.perform(delete("/api/v1/ohlcvdata/{id}", "testDummyId").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }
}
