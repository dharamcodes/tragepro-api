package com.tragepro.api.datafeed.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.entity.CandleEntity;
import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.common.model.response.CandleResponse;
import com.tragepro.api.datafeed.repository.CandleRepository;
import com.tragepro.api.datafeed.service.mapper.CandleMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandleServiceImplTest {

  @Mock private CandleRepository candleRepository;

  @Mock private MapperFactory<CandleMapper> mapperFactory;

  @Mock private CandleMapper candleMapper;

  @InjectMocks private CandleServiceImpl candleService;

  private CandleEntity candleEntity;
  private CandleRequest candleRequest;
  private CandleResponse candleResponse;

  @BeforeEach
  void setUp() {
    candleEntity = new CandleEntity();
    candleRequest = CandleRequest.builder().build();
    candleResponse = CandleResponse.builder().build();
  }

  @Test
  void isCandleExists_True() {
    when(candleRepository.existsBySymbolDataNameAndCandleDataTimestamp("Apple Inc.", 12345L))
        .thenReturn(true);

    boolean exists = candleService.isCandleExists("Apple Inc.", 12345L);

    assertTrue(exists);
    verify(candleRepository, times(1))
        .existsBySymbolDataNameAndCandleDataTimestamp("Apple Inc.", 12345L);
  }

  @Test
  void isCandleExists_False() {
    when(candleRepository.existsBySymbolDataNameAndCandleDataTimestamp("Apple Inc.", 12345L))
        .thenReturn(false);

    boolean exists = candleService.isCandleExists("Apple Inc.", 12345L);

    assertFalse(exists);
    verify(candleRepository, times(1))
        .existsBySymbolDataNameAndCandleDataTimestamp("Apple Inc.", 12345L);
  }

  @Test
  void create_Success() {
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    when(candleMapper.requestToEntity(any())).thenReturn(candleEntity);
    when(candleRepository.save(any())).thenReturn(candleEntity);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    CandleResponse response = candleService.create(candleRequest);

    assertNotNull(response);
    verify(candleRepository, times(1)).save(candleEntity);
  }

  @Test
  void getById_Success() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.of(candleEntity));
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    Optional<CandleResponse> response = candleService.getById("test-id");

    assertTrue(response.isPresent());
  }

  @Test
  void getById_NotFound() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> candleService.getById("test-id"));
  }

  @Test
  void delete_Success() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.of(candleEntity));
    doNothing().when(candleRepository).delete(any());

    assertDoesNotThrow(() -> candleService.delete("test-id"));
    verify(candleRepository, times(1)).delete(candleEntity);
  }

  @Test
  void delete_NotFound() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> candleService.delete("test-id"));
  }

  @Test
  void getAll_Success() {
    when(candleRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
        .thenReturn(
            new org.springframework.data.domain.PageImpl<>(java.util.List.of(candleEntity)));
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    org.springframework.data.domain.Page<CandleResponse> response =
        candleService.getAll(org.springframework.data.domain.Pageable.unpaged());

    assertNotNull(response);
    assertFalse(response.isEmpty());
  }

  @Test
  void getAll_Empty() {
    when(candleRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
        .thenReturn(org.springframework.data.domain.Page.empty());

    assertThrows(
        AppException.class,
        () -> candleService.getAll(org.springframework.data.domain.Pageable.unpaged()));
  }

  @Test
  void update_Success() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.of(candleEntity));
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    doNothing().when(candleMapper).merge(any(), any());
    when(candleRepository.save(any())).thenReturn(candleEntity);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    CandleResponse response = candleService.update("test-id", candleRequest);

    assertNotNull(response);
    verify(candleRepository, times(1)).save(candleEntity);
  }

  @Test
  void update_NotFound() {
    when(candleRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> candleService.update("test-id", candleRequest));
  }

  @Test
  void getCandlesBySymbolAndDaysBack_Success() {
    when(candleRepository.findBySymbolDataNameAndCandleDataTimestampGreaterThanEqual(
            anyString(), anyLong()))
        .thenReturn(java.util.List.of(candleEntity));
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    java.util.List<CandleResponse> response =
        candleService.getCandlesBySymbolAndDaysBack("AAPL", 5);

    assertNotNull(response);
    assertFalse(response.isEmpty());
  }

  @Test
  void getLatestCandlesBySymbols_Success() {
    java.util.Set<String> symbols = java.util.Set.of("AAPL");
    when(candleRepository.findLatestCandlesBySymbols(any()))
        .thenReturn(java.util.List.of(candleEntity));
    when(mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER)).thenReturn(candleMapper);
    when(candleMapper.entityToResponse(any())).thenReturn(candleResponse);

    java.util.Set<CandleResponse> response = candleService.getLatestCandlesBySymbols(symbols);

    assertNotNull(response);
    assertFalse(response.isEmpty());
  }

  @Test
  void getLatestCandlesBySymbols_Empty() {
    java.util.Set<String> symbols = java.util.Set.of("AAPL");
    when(candleRepository.findLatestCandlesBySymbols(any())).thenReturn(java.util.List.of());

    java.util.Set<CandleResponse> response = candleService.getLatestCandlesBySymbols(symbols);

    assertNotNull(response);
    assertTrue(response.isEmpty());
  }
}
