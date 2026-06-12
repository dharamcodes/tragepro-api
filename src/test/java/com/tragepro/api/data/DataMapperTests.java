package com.tragepro.api.data;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import com.tragepro.api.data.model.entity.CandleEntity;
import com.tragepro.api.data.model.entity.WatchListEntity;
import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.request.WatchListRequest;
import com.tragepro.api.data.model.response.CandleResponse;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.service.mapper.CandleMapper;
import com.tragepro.api.data.service.mapper.CandleMapperImpl;
import com.tragepro.api.data.service.mapper.WatchListMapper;
import com.tragepro.api.data.service.mapper.WatchListMapperImpl;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DataMapperTests {

    @Test
    void testCandleMapper() {
        CandleMapper mapper = new CandleMapperImpl();
        assertNull(mapper.requestToEntity(null));
        assertNull(mapper.entityToResponse(null));

        CandleEntity target = new CandleEntity();
        mapper.merge(null, target);

        // Test custom default methods exception path
        assertThrows(ServerException.class, () -> mapper.mapCandleData(null));
        assertThrows(ServerException.class, () -> mapper.mapSymbolData(null));

        // Test normal paths
        CandleData candleData = new CandleData(1600000000L, 10.0f, 12.0f, 9.0f, 11.0f, 1000L);
        SymbolData symbolData = new SymbolData("AAPL", "Apple");
        CandleRequest request = new CandleRequest(symbolData, candleData);

        CandleEntity entity = mapper.requestToEntity(request);
        assertNotNull(entity);
        assertEquals("AAPL", entity.getSymbolData().id());

        CandleResponse response = mapper.entityToResponse(entity);
        assertNotNull(response);
        assertEquals("AAPL", response.symbolData().id());

        CandleEntity merged = new CandleEntity();
        mapper.merge(request, merged);
        assertEquals("AAPL", merged.getSymbolData().id());
    }

    @Test
    void testWatchListMapper() {
        WatchListMapper mapper = new WatchListMapperImpl();
        assertNull(mapper.requestToEntity(null));
        assertNull(mapper.entityToResponse(null));

        WatchListEntity target = new WatchListEntity();
        mapper.merge(null, target);

        assertThrows(ServerException.class, () -> mapper.mapSymbolData(null));

        Set<SymbolData> stocks = new HashSet<>();
        stocks.add(new SymbolData("AAPL", "Apple"));
        WatchListRequest request = new WatchListRequest("name", "desc", stocks);

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
