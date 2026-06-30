package com.tragepro.api.strategy.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.strategy.constant.Exchange;
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
}
