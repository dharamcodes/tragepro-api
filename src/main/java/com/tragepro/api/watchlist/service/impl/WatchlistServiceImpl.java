package com.tragepro.api.watchlist.service.impl;

import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.repository.WatchlistRepository;
import com.tragepro.api.watchlist.service.WatchlistService;
import com.tragepro.api.watchlist.service.mapper.WatchlistMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository repository;
    private final WatchlistMapper mapper;

    @Override
    public WatchlistResponse create(String userId, WatchlistRequest request) {
        WatchlistEntity entity =
                WatchlistEntity.builder().userId(userId).name(request.name()).build();
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public List<WatchlistResponse> getAllForUser(String userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public WatchlistResponse getById(String id, String userId) {
        return mapper.toResponse(getWatchlistForUser(id, userId));
    }

    @Override
    public WatchlistResponse update(String id, String userId, WatchlistRequest request) {
        WatchlistEntity entity = getWatchlistForUser(id, userId);
        entity.setName(request.name());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(String id, String userId) {
        WatchlistEntity entity = getWatchlistForUser(id, userId);
        repository.delete(entity);
    }

    @Override
    public WatchlistResponse addSymbol(String id, String userId, String symbolId) {
        WatchlistEntity entity = getWatchlistForUser(id, userId);
        entity.getSymbols().add(symbolId);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public WatchlistResponse removeSymbol(String id, String userId, String symbolId) {
        WatchlistEntity entity = getWatchlistForUser(id, userId);
        entity.getSymbols().remove(symbolId);
        return mapper.toResponse(repository.save(entity));
    }

    private WatchlistEntity getWatchlistForUser(String id, String userId) {
        WatchlistEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("Watchlist not found for id: {}", id);
            return new AppException(ErrorType.DATA_NOT_FOUND);
        });
        if (!entity.getUserId().equals(userId)) {
            log.error("Watchlist {} does not belong to user {}", id, userId);
            throw new AppException(
                    ErrorType.ACCESS_DENIED); // Could also be FORBIDDEN or NOT_FOUND to avoid leaking existence
        }
        return entity;
    }
}
