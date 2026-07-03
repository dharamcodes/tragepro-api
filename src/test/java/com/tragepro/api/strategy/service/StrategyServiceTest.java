package com.tragepro.api.strategy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.util.CloneUtil;
import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.model.StatusModel;
import com.tragepro.api.strategy.model.StrategyModel;
import com.tragepro.api.strategy.model.SymbolModel;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.repository.StrategyRepository;
import com.tragepro.api.strategy.service.impl.StrategyServiceImpl;
import com.tragepro.api.strategy.service.mapper.StrategyMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyServiceTest {

  @Mock private StrategyRepository strategyRepository;
  @Mock private MapperFactory<StrategyMapper> mapperFactory;
  @Mock private CloneUtil cloneUtil;
  @Mock private StrategyMapper strategyMapper;

  @InjectMocks private StrategyServiceImpl strategyService;

  private StrategyRequest validRequest;
  private StrategyEntity mockEntity;
  private StrategyResponse mockResponse;

  @BeforeEach
  void setUp() {
    lenient()
        .when(mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER))
        .thenReturn(strategyMapper);

    validRequest =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().watchlist("WL").build())
            .symbolData(SymbolModel.builder().symbol("SYM").build())
            .currentState(StatusModel.builder().state(StrategyState.INITIALIZING).build())
            .build();

    mockEntity = new StrategyEntity();
    mockResponse = StrategyResponse.builder().build();
  }

  @Test
  void testCreate_NullRequest_ThrowsException() {
    AppException exception = assertThrows(AppException.class, () -> strategyService.create(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreate_Success() {
    when(strategyMapper.requestToEntity(validRequest)).thenReturn(mockEntity);
    when(strategyRepository.save(mockEntity)).thenReturn(mockEntity);
    when(strategyMapper.entityToResponse(mockEntity)).thenReturn(mockResponse);

    StrategyResponse result = strategyService.create(validRequest);
    assertNotNull(result);
    assertEquals(mockResponse, result);
    verify(strategyRepository, times(1)).save(mockEntity);
  }

  @Test
  void testCreateOrUpdate_NullRequest_ThrowsException() {
    AppException exception =
        assertThrows(AppException.class, () -> strategyService.createOrUpdate(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreateOrUpdate_NullStrategy_ThrowsException() {
    validRequest.setStrategy(null);
    AppException exception =
        assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreateOrUpdate_NullSymbolData_ThrowsException() {
    validRequest.setSymbolData(null);
    AppException exception =
        assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreateOrUpdate_NullCurrentState_ThrowsException() {
    validRequest.setCurrentState(null);
    AppException exception =
        assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreateOrUpdate_NullNestedValues_ThrowsException() {
    validRequest.getStrategy().setWatchlist(null);
    AppException exception =
        assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testCreateOrUpdate_NotFound_CreatesNew() {
    when(strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
            "WL", "SYM", StrategyState.INITIALIZING))
        .thenReturn(Optional.empty());

    when(strategyMapper.requestToEntity(validRequest)).thenReturn(mockEntity);
    when(strategyRepository.save(mockEntity)).thenReturn(mockEntity);
    when(strategyMapper.entityToResponse(mockEntity)).thenReturn(mockResponse);

    StrategyResponse result = strategyService.createOrUpdate(validRequest);
    assertNotNull(result);
    assertEquals(mockResponse, result);
    verify(strategyRepository, times(1)).save(mockEntity);
  }

  @Test
  void testCreateOrUpdate_FoundIdentical_BypassesSave() {
    StrategyEntity existingEntity = new StrategyEntity();
    StrategyEntity clonedEntity = new StrategyEntity();

    when(strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
            "WL", "SYM", StrategyState.INITIALIZING))
        .thenReturn(Optional.of(existingEntity));
    when(cloneUtil.clone(existingEntity, StrategyEntity.class)).thenReturn(clonedEntity);
    doNothing().when(strategyMapper).merge(validRequest, clonedEntity);
    when(strategyMapper.entityToResponse(existingEntity)).thenReturn(mockResponse);

    StrategyResponse result = strategyService.createOrUpdate(validRequest);
    assertNotNull(result);
    assertEquals(mockResponse, result);
    verify(strategyRepository, never()).save(any());
  }

  @Test
  void testCreateOrUpdate_FoundDifferent_SavesMerged() {
    StrategyEntity existingEntity = new StrategyEntity();
    StrategyEntity clonedEntity = new StrategyEntity();
    clonedEntity.setId("changed-id");

    when(strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
            "WL", "SYM", StrategyState.INITIALIZING))
        .thenReturn(Optional.of(existingEntity));
    when(cloneUtil.clone(existingEntity, StrategyEntity.class)).thenReturn(clonedEntity);
    doNothing().when(strategyMapper).merge(validRequest, clonedEntity);
    when(strategyRepository.save(clonedEntity)).thenReturn(clonedEntity);
    when(strategyMapper.entityToResponse(clonedEntity)).thenReturn(mockResponse);

    StrategyResponse result = strategyService.createOrUpdate(validRequest);
    assertNotNull(result);
    assertEquals(mockResponse, result);
    verify(strategyRepository, times(1)).save(clonedEntity);
  }
}
