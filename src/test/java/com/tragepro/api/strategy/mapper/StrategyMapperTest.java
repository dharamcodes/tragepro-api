package com.tragepro.api.strategy.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.strategy.model.StrategyModel;
import com.tragepro.api.strategy.model.SymbolModel;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.service.mapper.StrategyMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class StrategyMapperTest {

  private final StrategyMapper mapper = Mappers.getMapper(StrategyMapper.class);

  @Test
  void testGetType() {
    assertEquals(MapperType.STRATEGY_BUILDER_MAPPER, mapper.getType());
  }

  @Test
  void testRequestToEntity_NullAndEmpty() {
    assertNull(mapper.requestToEntity(null));
    StrategyEntity entity = mapper.requestToEntity(StrategyRequest.builder().build());
    assertNotNull(entity);
  }

  @Test
  void testRequestToEntity_FullyPopulated() {
    StrategyModel strategy = StrategyModel.builder().name("Test Strategy").desc("Desc").build();
    SymbolModel symbolData =
        SymbolModel.builder().symbol("AAPL").name("Apple").exchange(Exchange.NSE).build();

    StrategyRequest request =
        StrategyRequest.builder().strategy(strategy).symbolData(symbolData).build();

    StrategyEntity entity = mapper.requestToEntity(request);
    assertNotNull(entity);
    assertNotNull(entity.getStrategy());
    assertEquals("Test Strategy", entity.getStrategy().getName());
    assertEquals("Desc", entity.getStrategy().getDesc());
    assertNotNull(entity.getSymbolData());
    assertEquals("AAPL", entity.getSymbolData().getSymbol());
    assertEquals("Apple", entity.getSymbolData().getName());
    assertEquals(Exchange.NSE, entity.getSymbolData().getExchange());
  }

  @Test
  void testEntityToResponse_NullAndEmpty() {
    assertNull(mapper.entityToResponse(null));
    StrategyResponse response = mapper.entityToResponse(new StrategyEntity());
    assertNotNull(response);
  }

  @Test
  void testEntityToResponse_FullyPopulated() {
    StrategyModel strategy = StrategyModel.builder().name("Test").desc("Desc").build();
    SymbolModel symbolData =
        SymbolModel.builder().symbol("AAPL").name("Apple").exchange(Exchange.NSE).build();

    StrategyEntity entity = new StrategyEntity();
    entity.setId("123");
    entity.setStrategy(strategy);
    entity.setSymbolData(symbolData);

    StrategyResponse response = mapper.entityToResponse(entity);
    assertNotNull(response);
    assertNotNull(response.getStrategy());
    assertEquals("Test", response.getStrategy().getName());
    assertEquals("Desc", response.getStrategy().getDesc());
    assertNotNull(response.getSymbolData());
    assertEquals("AAPL", response.getSymbolData().getSymbol());
    assertEquals("Apple", response.getSymbolData().getName());
    assertEquals(Exchange.NSE, response.getSymbolData().getExchange());
  }

  @Test
  void testMerge() {
    StrategyModel strategy = StrategyModel.builder().name("Updated Name").desc("Updated").build();
    SymbolModel symbolData =
        SymbolModel.builder()
            .symbol("Updated Symbol")
            .name("Updated")
            .exchange(Exchange.NSE)
            .build();

    StrategyRequest request =
        StrategyRequest.builder().strategy(strategy).symbolData(symbolData).build();

    StrategyEntity target = new StrategyEntity();
    target.setId("123");
    target.setStrategy(StrategyModel.builder().name("Old Name").desc("Old").build());
    target.setSymbolData(
        SymbolModel.builder().symbol("Old Symbol").name("Old").exchange(Exchange.NSE).build());

    mapper.merge(request, target);

    assertEquals("123", target.getId());
    assertNotNull(target.getStrategy());
    assertEquals("Updated Name", target.getStrategy().getName());
    assertEquals("Updated", target.getStrategy().getDesc());
    assertNotNull(target.getSymbolData());
    assertEquals("Updated Symbol", target.getSymbolData().getSymbol());
    assertEquals("Updated", target.getSymbolData().getName());
  }

  @Test
  void testMerge_Null() {
    StrategyEntity target = new StrategyEntity();
    target.setStrategy(StrategyModel.builder().name("Old Name").build());
    mapper.merge(null, target);
    assertNotNull(target.getStrategy());
    assertEquals("Old Name", target.getStrategy().getName());
  }

  @Test
  void testToSymbolData() {
    SymbolModel symbolModel = SymbolModel.builder().symbol("AAPL").name("Apple").build();
    SymbolDataModel symbolData = mapper.toSymbolData(symbolModel);
    assertNotNull(symbolData);
    assertEquals("AAPL", symbolData.symbol());
    assertEquals("Apple", symbolData.name());
  }

  @Test
  void testResponseToRequest() {
    assertNull(mapper.responseToRequest(null));

    StrategyResponse response =
        StrategyResponse.builder().strategy(StrategyModel.builder().name("Test").build()).build();
    StrategyRequest request = mapper.responseToRequest(response);
    assertNotNull(request);
    assertNotNull(request.getStrategy());
    assertEquals("Test", request.getStrategy().getName());
  }

  @Test
  void testPartialNulls() {
    StrategyRequest req1 = StrategyRequest.builder().strategy(null).symbolData(null).build();
    StrategyEntity ent1 = mapper.requestToEntity(req1);
    assertNotNull(ent1);
    assertNull(ent1.getStrategy());
    assertNull(ent1.getSymbolData());

    StrategyRequest req2 =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().build())
            .symbolData(null)
            .build();
    StrategyEntity ent2 = mapper.requestToEntity(req2);
    assertNotNull(ent2);
    assertNotNull(ent2.getStrategy());
    assertNull(ent2.getSymbolData());

    StrategyRequest req3 =
        StrategyRequest.builder().strategy(null).symbolData(SymbolModel.builder().build()).build();
    StrategyEntity ent3 = mapper.requestToEntity(req3);
    assertNotNull(ent3);
    assertNull(ent3.getStrategy());
    assertNotNull(ent3.getSymbolData());

    StrategyEntity ent4 = new StrategyEntity();
    ent4.setStrategy(null);
    ent4.setSymbolData(null);
    StrategyResponse resp4 = mapper.entityToResponse(ent4);
    assertNotNull(resp4);
    assertNull(resp4.getStrategy());
    assertNull(resp4.getSymbolData());

    StrategyRequest req5 = StrategyRequest.builder().strategy(null).symbolData(null).build();
    StrategyEntity ent5 = new StrategyEntity();
    ent5.setStrategy(StrategyModel.builder().name("Old").build());
    mapper.merge(req5, ent5);

    StrategyResponse resp6 = StrategyResponse.builder().strategy(null).symbolData(null).build();
    StrategyRequest req6 = mapper.responseToRequest(resp6);
    assertNotNull(req6);
    assertNull(req6.getStrategy());
    assertNull(req6.getSymbolData());

    assertThrows(NullPointerException.class, () -> mapper.toSymbolData(null));
  }
}
