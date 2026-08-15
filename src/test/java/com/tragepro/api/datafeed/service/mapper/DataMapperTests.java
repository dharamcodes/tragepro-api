package com.tragepro.api.datafeed.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.Exchange;
import com.tragepro.api.domain.datafeed.entity.CandleEntity;
import com.tragepro.api.domain.datafeed.entity.WatchListEntity;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DataMapperTests {

  @Test
  void testCandleMapper() {
    CandleMapper mapper = org.mapstruct.factory.Mappers.getMapper(CandleMapper.class);
    assertNull(mapper.requestToEntity(null));
    assertNull(mapper.entityToResponse(null));

    CandleEntity target = new CandleEntity();
    mapper.merge(null, target);

    // Test custom default methods exception path
    assertThrows(ServerException.class, () -> mapper.mapCandleData(null));
    assertThrows(ServerException.class, () -> mapper.mapSymbolData(null));

    // Test normal paths
    CandleDataModel candleData = new CandleDataModel(1600000000L, 10.0f, 12.0f, 9.0f, 11.0f, 1000L);
    SymbolDataModel symbolData = new SymbolDataModel("AAPL", "Apple");
    CandleRequest request = new CandleRequest(symbolData, candleData);

    CandleEntity entity = mapper.requestToEntity(request);
    assertNotNull(entity);
    assertEquals("AAPL", entity.getSymbolData().symbol());

    CandleResponse response = mapper.entityToResponse(entity);
    assertNotNull(response);
    assertEquals("AAPL", response.symbolData().symbol());

    CandleEntity merged = new CandleEntity();
    mapper.merge(request, merged);
    assertEquals("AAPL", merged.getSymbolData().symbol());
  }

  @Test
  void testWatchListMapper() {
    WatchListMapper mapper = org.mapstruct.factory.Mappers.getMapper(WatchListMapper.class);
    assertNull(mapper.requestToEntity(null));
    assertNull(mapper.entityToResponse(null));

    WatchListEntity target = new WatchListEntity();
    mapper.merge(null, target);

    assertThrows(ServerException.class, () -> mapper.mapSymbolData(null));
    assertDoesNotThrow(() -> mapper.mapSymbolData(new SymbolDataModel("AAPL", "Apple")));

    Set<SymbolDataModel> stocks = new HashSet<>();
    stocks.add(new SymbolDataModel("AAPL", "Apple"));
    WatchListRequest request = new WatchListRequest("name", "desc", Exchange.NSE, stocks);

    WatchListEntity entity = mapper.requestToEntity(request);
    assertNotNull(entity);
    assertEquals("name", entity.getName());

    WatchListResponse response = mapper.entityToResponse(entity);
    assertNotNull(response);
    assertEquals("name", response.name());

    WatchListEntity merged = new WatchListEntity();
    mapper.merge(request, merged);
    assertEquals("name", merged.getName());
  }
}
