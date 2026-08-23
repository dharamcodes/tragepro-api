package com.tragepro.api.strategy.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.domain.strategy.StatusModel;
import com.tragepro.api.domain.strategy.StrategyModel;
import com.tragepro.api.domain.strategy.SymbolModel;
import com.tragepro.api.domain.strategy.constant.StrategyState;
import com.tragepro.api.domain.strategy.entity.StrategyEntity;
import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.repository.StrategyRepository;
import com.tragepro.api.strategy.service.mapper.StrategyMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyServiceImplTest {

    @Mock
    private StrategyRepository strategyRepository;

    @Mock
    private MapperFactory mapperFactory;

    @Mock
    private StrategyMapper strategyMapper;

    @InjectMocks
    private StrategyServiceImpl strategyService;

    private StrategyRequest validRequest;
    private StrategyEntity mockEntity;
    private StrategyResponse mockResponse;

    @BeforeEach
    void setUp() {
        lenient().when(mapperFactory.getMapper(StrategyMapper.class)).thenReturn(strategyMapper);

        validRequest = StrategyRequest.builder()
                .strategy(StrategyModel.builder().watchlist("WL").build())
                .symbolData(SymbolModel.builder().symbol("SYM").build())
                .currentState(
                        StatusModel.builder().state(StrategyState.INITIALIZING).build())
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
        AppException exception = assertThrows(AppException.class, () -> strategyService.createOrUpdate(null));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
    }

    @Test
    void testCreateOrUpdate_NullStrategy_ThrowsException() {
        validRequest.setStrategy(null);
        AppException exception = assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
    }

    @Test
    void testCreateOrUpdate_NullSymbolData_ThrowsException() {
        validRequest.setSymbolData(null);
        AppException exception = assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
    }

    @Test
    void testCreateOrUpdate_NullCurrentState_ThrowsException() {
        validRequest.setCurrentState(null);
        AppException exception = assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
    }

    @Test
    void testCreateOrUpdate_NullNestedValues_ThrowsException() {
        validRequest.getStrategy().setWatchlist(null);
        AppException exception = assertThrows(AppException.class, () -> strategyService.createOrUpdate(validRequest));
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

        when(strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
                        "WL", "SYM", StrategyState.INITIALIZING))
                .thenReturn(Optional.of(existingEntity));
        doNothing().when(strategyMapper).merge(any(StrategyRequest.class), any(StrategyEntity.class));
        when(strategyMapper.entityToResponse(existingEntity)).thenReturn(mockResponse);

        StrategyResponse result = strategyService.createOrUpdate(validRequest);
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(strategyRepository, never()).save(any());
    }

    @Test
    void testCreateOrUpdate_FoundDifferent_SavesMerged() {
        StrategyEntity existingEntity = new StrategyEntity();

        when(strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
                        "WL", "SYM", StrategyState.INITIALIZING))
                .thenReturn(Optional.of(existingEntity));
        doAnswer(invocation -> {
                    StrategyEntity entity = invocation.getArgument(1);
                    entity.setId("changed-id");
                    return null;
                })
                .when(strategyMapper)
                .merge(any(StrategyRequest.class), any(StrategyEntity.class));
        when(strategyRepository.save(any(StrategyEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(strategyMapper.entityToResponse(any(StrategyEntity.class))).thenReturn(mockResponse);

        StrategyResponse result = strategyService.createOrUpdate(validRequest);
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(strategyRepository, times(1)).save(any(StrategyEntity.class));
    }
}
