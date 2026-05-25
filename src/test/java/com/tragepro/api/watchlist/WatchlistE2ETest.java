package com.tragepro.api.watchlist;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.repository.CandleRepository;
import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class WatchlistE2ETest extends ContainerConfig {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CandleRepository candleRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        candleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "e2eUser")
    void testWatchlistE2EFlow() throws Exception {
        // 1. Create a watchlist
        WatchlistRequest req = new WatchlistRequest("E2E Watchlist");
        MvcResult result = mockMvc.perform(post("/api/v1/watchlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("E2E Watchlist"))
                .andReturn();

        WatchlistResponse created =
                objectMapper.readValue(result.getResponse().getContentAsString(), WatchlistResponse.class);
        String watchlistId = created.id();

        // 2. Add symbols
        mockMvc.perform(post("/api/v1/watchlists/{id}/symbols/BTCUSD", watchlistId))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/watchlists/{id}/symbols/ETHUSD", watchlistId))
                .andExpect(status().isOk());

        // 3. Seed Candle data (Testcontainers MongoDB)
        long now = System.currentTimeMillis();
        Symbol btcSymbol = new Symbol("BTCUSD", "Bitcoin");
        Candle btcCandleData = new Candle(now, 90, 120, 85, 110, 500);
        CandleEntity btcCandle = new CandleEntity(null, btcSymbol, btcCandleData);

        Symbol ethSymbol = new Symbol("ETHUSD", "Ethereum");
        Candle ethCandleData = new Candle(now, 20, 30, 15, 25, 1000);
        CandleEntity ethCandle = new CandleEntity(null, ethSymbol, ethCandleData);
        candleRepository.saveAll(List.of(btcCandle, ethCandle));

        // 4. Get market data sorted by volume descending
        result = mockMvc.perform(
                        get("/api/v1/watchlists/{id}/market-data", watchlistId).param("sort", "volume,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        List<WatchlistMarketDataResponse> marketData = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<WatchlistMarketDataResponse>>() {});

        // ETHUSD has volume 1000, BTCUSD has volume 500
        assertEquals("ETHUSD", marketData.get(0).symbolId());
        assertEquals(25.0, marketData.get(0).lastTradedPrice());
        assertEquals("BTCUSD", marketData.get(1).symbolId());

        // 5. Get market data sorted by price ascending (lastTradedPrice maps to close)
        result = mockMvc.perform(
                        get("/api/v1/watchlists/{id}/market-data", watchlistId).param("sort", "price,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        marketData = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<WatchlistMarketDataResponse>>() {});

        // ETHUSD is 25, BTCUSD is 110
        assertEquals("ETHUSD", marketData.get(0).symbolId());
        assertEquals("BTCUSD", marketData.get(1).symbolId());
    }
}
