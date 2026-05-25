package com.tragepro.api.watchlist.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.repository.WatchlistRepository;
import com.tragepro.api.watchlist.service.mapper.WatchlistMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceImplTest {

    @Mock
    private WatchlistRepository repository;

    @Mock
    private WatchlistMapper mapper;

    @InjectMocks
    private WatchlistServiceImpl watchlistService;

    private WatchlistEntity entity;
    private WatchlistResponse response;
    private final String userId = "user123";
    private final String watchlistId = "wl123";

    @BeforeEach
    void setUp() {
        entity = WatchlistEntity.builder()
                .id(watchlistId)
                .userId(userId)
                .name("My Watchlist")
                .symbols(new HashSet<>(Set.of("BTCUSD")))
                .build();
        response = new WatchlistResponse(watchlistId, "My Watchlist", Set.of("BTCUSD"));
    }

    @Test
    void create() {
        WatchlistRequest req = new WatchlistRequest("My Watchlist");
        when(repository.save(any(WatchlistEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        WatchlistResponse res = watchlistService.create(userId, req);
        assertEquals("My Watchlist", res.name());
        verify(repository).save(any(WatchlistEntity.class));
    }

    @Test
    void getAllForUser() {
        when(repository.findByUserId(userId)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<WatchlistResponse> list = watchlistService.getAllForUser(userId);
        assertEquals(1, list.size());
    }

    @Test
    void getById_Success() {
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        WatchlistResponse res = watchlistService.getById(watchlistId, userId);
        assertEquals(watchlistId, res.id());
    }

    @Test
    void getById_NotFound() {
        when(repository.findById(watchlistId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> watchlistService.getById(watchlistId, userId));
        assertEquals(ErrorType.DATA_NOT_FOUND, ex.getErrorType());
    }

    @Test
    void getById_Unauthorized() {
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));

        AppException ex = assertThrows(AppException.class, () -> watchlistService.getById(watchlistId, "otherUser"));
        assertEquals(ErrorType.ACCESS_DENIED, ex.getErrorType());
    }

    @Test
    void update() {
        WatchlistRequest req = new WatchlistRequest("Updated");
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));
        when(repository.save(any(WatchlistEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(new WatchlistResponse(watchlistId, "Updated", Set.of("BTCUSD")));

        WatchlistResponse res = watchlistService.update(watchlistId, userId, req);
        assertEquals("Updated", res.name());
    }

    @Test
    void delete() {
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));
        watchlistService.delete(watchlistId, userId);
        verify(repository).delete(entity);
    }

    @Test
    void addSymbol() {
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));
        when(repository.save(any(WatchlistEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        WatchlistResponse res = watchlistService.addSymbol(watchlistId, userId, "ETHUSD");
        assertNotNull(res);
        assertTrue(entity.getSymbols().contains("ETHUSD"));
    }

    @Test
    void removeSymbol() {
        when(repository.findById(watchlistId)).thenReturn(Optional.of(entity));
        when(repository.save(any(WatchlistEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        WatchlistResponse res = watchlistService.removeSymbol(watchlistId, userId, "BTCUSD");
        assertNotNull(res);
        assertFalse(entity.getSymbols().contains("BTCUSD"));
    }
}
