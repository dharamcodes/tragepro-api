package com.tragepro.api.watchlist.service.mapper;

import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WatchlistMapper {

    WatchlistResponse toResponse(WatchlistEntity entity);
}
