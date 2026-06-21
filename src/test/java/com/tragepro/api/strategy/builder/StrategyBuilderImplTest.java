package com.tragepro.api.strategy.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.strategy.constant.BuilderSteps;
import com.tragepro.api.strategy.mapper.StrategyBuilderMapper;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyBuilderImplTest {

  @Mock private MapperFactory<StrategyBuilderMapper> mapperFactory;

  @Mock private StrategyBuilderMapper mapper;

  @InjectMocks private StrategyBuilderImpl strategyBuilder;

  @Test
  void testBuild() {
    when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER)).thenReturn(mapper);

    StrategyEntity entity = new StrategyEntity();
    StrategyResponse response = StrategyResponse.builder().build();

    when(mapper.requestToEntity(any(StrategyRequest.class))).thenReturn(entity);
    when(mapper.entityToResponse(entity)).thenReturn(response);

    StrategyResponse result = strategyBuilder.build();
    assertNotNull(result);
  }

  @Test
  void testStrategyFactory_PopulateData() {
    when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER)).thenReturn(mapper);
    StrategyEntity entity = new StrategyEntity();
    StrategyResponse response = StrategyResponse.builder().build();

    when(mapper.requestToEntity(any(StrategyRequest.class))).thenReturn(entity);
    when(mapper.entityToResponse(entity)).thenReturn(response);

    StrategyResponse result = strategyBuilder.strategyFactory(BuilderSteps.POPULATE_DATA);
    assertNotNull(result);
  }

  @Test
  void testStrategyFactory_PopulateMeta() {
    when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER)).thenReturn(mapper);
    StrategyEntity entity = new StrategyEntity();
    StrategyResponse response = StrategyResponse.builder().build();

    when(mapper.requestToEntity(any(StrategyRequest.class))).thenReturn(entity);
    when(mapper.entityToResponse(entity)).thenReturn(response);

    StrategyResponse result = strategyBuilder.strategyFactory(BuilderSteps.POPULATE_META);
    assertNotNull(result);
  }

  @Test
  void testStrategyFactory_PopulateIndicator() {
    when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER)).thenReturn(mapper);
    StrategyEntity entity = new StrategyEntity();
    StrategyResponse response = StrategyResponse.builder().build();

    when(mapper.requestToEntity(any(StrategyRequest.class))).thenReturn(entity);
    when(mapper.entityToResponse(entity)).thenReturn(response);

    StrategyResponse result = strategyBuilder.strategyFactory(BuilderSteps.POPULATE_INDICATOR);
    assertNotNull(result);
  }

  @Test
  void testStrategyFactory_Null() {
    when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER)).thenReturn(mapper);
    AppException ex = assertThrows(AppException.class, () -> strategyBuilder.strategyFactory(null));
    assertEquals(ErrorType.DATA_NOT_FOUND, ex.getErrorType());
  }
}
