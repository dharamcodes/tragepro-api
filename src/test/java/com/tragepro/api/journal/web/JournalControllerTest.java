package com.tragepro.api.journal.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.enums.TradeStatus;
import com.tragepro.api.journal.model.enums.TradeType;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.repository.JournalRepository;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class JournalControllerTest extends ApiTestSetup {

    @Autowired
    private JournalRepository journalRepository;

    private JournalRequest request;
    private String savedId;
    private final String accountId = "testAccId";

    @BeforeEach
    void setUp() {
        journalRepository.deleteAll();

        request = JournalRequest.builder()
                .accountId(accountId)
                .symbol("AAPL")
                .tradeType(TradeType.LONG)
                .status(TradeStatus.OPEN)
                .entryPrice(BigDecimal.valueOf(150))
                .quantity(BigDecimal.valueOf(10))
                .entryTime(Instant.now())
                .build();

        JournalEntity entity = JournalEntity.builder()
                .accountId(accountId)
                .symbol("AAPL")
                .tradeType(TradeType.LONG)
                .status(TradeStatus.OPEN)
                .entryPrice(BigDecimal.valueOf(150))
                .quantity(BigDecimal.valueOf(10))
                .entryTime(Instant.now())
                .build();

        JournalEntity savedEntity = journalRepository.save(entity);
        savedId = savedEntity.getId();
    }

    @Test
    void testCreateJournal_Success() throws Exception {
        mockMvc.perform(post("/api/v1/journal")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.accountId").value(accountId));
    }

    @Test
    void testCreateJournal_ValidationFailure() throws Exception {
        JournalRequest badRequest = JournalRequest.builder().build();

        mockMvc.perform(post("/api/v1/journal")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetJournalById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/journal/{id}", savedId).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.symbol").value("AAPL"));
    }

    @Test
    void testGetJournalById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/journal/{id}", "nonExistentId").header("Authorization", authToken))
                .andExpect(status().isNotFound()); // Or whatever status AppException mapper returns for DATA_NOT_FOUND
    }

    @Test
    void testGetAllJournals_Success() throws Exception {
        mockMvc.perform(get("/api/v1/journal")
                        .header("Authorization", authToken)
                        .param("accountId", accountId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "entryTime,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(savedId))
                .andExpect(jsonPath("$.content[0].accountId").value(accountId));
    }

    @Test
    void testUpdateJournal_Success() throws Exception {
        JournalRequest updateRequest = JournalRequest.builder()
                .accountId(accountId)
                .symbol("MSFT")
                .tradeType(TradeType.SHORT)
                .status(TradeStatus.CLOSED)
                .entryPrice(BigDecimal.valueOf(200))
                .quantity(BigDecimal.valueOf(15))
                .entryTime(Instant.now())
                .build();

        mockMvc.perform(put("/api/v1/journal/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.symbol").value("MSFT"));
    }

    @Test
    void testUpdateJournal_ValidationFailure() throws Exception {
        JournalRequest badRequest = JournalRequest.builder().build();

        mockMvc.perform(put("/api/v1/journal/{id}", savedId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }
}
