package com.tragepro.api.strategy.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class StrategyBuilderMapperTest {

  private final StrategyBuilderMapper mapper = Mappers.getMapper(StrategyBuilderMapper.class);

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
    StrategyRequest request =
        StrategyRequest.builder().name("Test Strategy").symbol("AAPL").build();
    StrategyEntity entity = mapper.requestToEntity(request);
    assertNotNull(entity);
    assertEquals("Test Strategy", entity.getName());
    assertEquals("AAPL", entity.getSymbol());
  }

  @Test
  void testEntityToResponse_NullAndEmpty() {
    assertNull(mapper.entityToResponse(null));
    StrategyResponse response = mapper.entityToResponse(new StrategyEntity());
    assertNotNull(response);
  }

  @Test
  void testEntityToResponse_FullyPopulated() {
    StrategyEntity entity = new StrategyEntity();
    entity.setId("123");
    entity.setName("Test");
    entity.setSymbol("AAPL");

    StrategyResponse response = mapper.entityToResponse(entity);
    assertNotNull(response);
    assertEquals("123", response.getId());
    assertEquals("Test", response.getName());
    assertEquals("AAPL", response.getSymbol());
  }

  @Test
  void testMerge() {
    StrategyRequest request =
        StrategyRequest.builder().name("Updated Name").symbol("Updated Symbol").build();

    StrategyEntity target = new StrategyEntity();
    target.setId("123");
    target.setName("Old Name");
    target.setSymbol("Old Symbol");

    mapper.merge(request, target);

    assertEquals("123", target.getId());
    assertEquals("Updated Name", target.getName());
    assertEquals("Updated Symbol", target.getSymbol());
  }

  @Test
  void testMerge_Null() {
    StrategyEntity target = new StrategyEntity();
    target.setName("Old Name");
    mapper.merge(null, target);
    assertEquals("Old Name", target.getName());
  }
}
