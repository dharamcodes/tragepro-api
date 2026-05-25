package com.tragepro.api.watchlist.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.service.WatchlistMarketDataService;
import com.tragepro.api.watchlist.service.WatchlistService;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class WatchlistControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private WatchlistService watchlistService;

    @Mock
    private WatchlistMarketDataService watchlistMarketDataService;

    @InjectMocks
    private WatchlistController controller;

    private WatchlistResponse response;
    private final String userId = "user123";
    private final String watchlistId = "wl123";

    private Principal principal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new org.springframework.data.web.SortHandlerMethodArgumentResolver())
                .build();
        response = new WatchlistResponse(watchlistId, "My Watchlist", Set.of("BTCUSD"));
        principal = () -> userId;
    }

    @Test
    void create() throws Exception {
        WatchlistRequest req = new WatchlistRequest("My Watchlist");
        when(watchlistService.create(eq(userId), any(WatchlistRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/watchlists")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("My Watchlist"));
    }

    @Test
    void getAll() throws Exception {
        when(watchlistService.getAllForUser(userId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/watchlists").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getById() throws Exception {
        when(watchlistService.getById(watchlistId, userId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/watchlists/{id}", watchlistId).principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(watchlistId));
    }

    @Test
    void update() throws Exception {
        WatchlistRequest req = new WatchlistRequest("Updated");
        WatchlistResponse updated = new WatchlistResponse(watchlistId, "Updated", Set.of("BTCUSD"));
        when(watchlistService.update(eq(watchlistId), eq(userId), any(WatchlistRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/watchlists/{id}", watchlistId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteWatchlist() throws Exception {
        doNothing().when(watchlistService).delete(watchlistId, userId);

        mockMvc.perform(delete("/api/v1/watchlists/{id}", watchlistId).principal(principal))
                .andExpect(status().isNoContent());
    }

    @Test
    void addSymbol() throws Exception {
        when(watchlistService.addSymbol(watchlistId, userId, "ETHUSD")).thenReturn(response);

        mockMvc.perform(post("/api/v1/watchlists/{id}/symbols/{symbolId}", watchlistId, "ETHUSD")
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    void removeSymbol() throws Exception {
        when(watchlistService.removeSymbol(watchlistId, userId, "BTCUSD")).thenReturn(response);

        mockMvc.perform(delete("/api/v1/watchlists/{id}/symbols/{symbolId}", watchlistId, "BTCUSD")
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    void getMarketData() throws Exception {
        WatchlistMarketDataResponse marketData =
                new WatchlistMarketDataResponse("BTCUSD", "Bitcoin", 100, 110, 500, 110);
        when(watchlistMarketDataService.getMarketDataForWatchlist(eq(watchlistId), eq(userId), any(Sort.class)))
                .thenReturn(List.of(marketData));

        mockMvc.perform(get("/api/v1/watchlists/{id}/market-data", watchlistId)
                        .principal(principal)
                        .param("sort", "volume,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].symbolId").value("BTCUSD"));
    }
}
