package com.tragepro.api.datafeed.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.datafeed.model.entity.WatchListEntity;
import com.tragepro.api.datafeed.model.request.WatchListRequest;
import com.tragepro.api.datafeed.model.response.WatchListResponse;
import com.tragepro.api.datafeed.repository.WatchListRepository;
import com.tragepro.api.datafeed.service.mapper.WatchListMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class WatchListServiceImplTest {

  @Mock private WatchListRepository watchListRepository;
  @Mock private MapperFactory<WatchListMapper> mapperFactory;
  @Mock private WatchListMapper watchListMapper;

  @InjectMocks private WatchListServiceImpl watchListService;

  private WatchListRequest request;
  private WatchListEntity entity;
  private WatchListResponse response;

  @BeforeEach
  void setUp() {
    request = WatchListRequest.builder().name("WL1").build();
    entity = new WatchListEntity();
    entity.setId("id1");
    response = WatchListResponse.builder().id("id1").name("WL1").build();
    lenient()
        .when(mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER))
        .thenReturn(watchListMapper);
  }

  @Test
  void testCreate() {
    when(watchListMapper.requestToEntity(request)).thenReturn(entity);
    when(watchListRepository.save(entity)).thenReturn(entity);
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    WatchListResponse result = watchListService.create(request);
    assertEquals(response, result);
  }

  @Test
  void testGetById_Success() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.of(entity));
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    Optional<WatchListResponse> result = watchListService.getById("id1");
    assertTrue(result.isPresent());
    assertEquals(response, result.get());
  }

  @Test
  void testGetById_NotFound() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> watchListService.getById("id1"));
  }

  @Test
  void testGetAllPageable_Success() {
    Page<WatchListEntity> page = new PageImpl<>(List.of(entity));
    when(watchListRepository.getWatchListSummery(any(Pageable.class))).thenReturn(page);
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    Page<WatchListResponse> result = watchListService.getAll(Pageable.unpaged());
    assertEquals(1, result.getTotalElements());
  }

  @Test
  void testGetAllPageable_Empty() {
    when(watchListRepository.getWatchListSummery(any(Pageable.class))).thenReturn(Page.empty());
    assertThrows(AppException.class, () -> watchListService.getAll(Pageable.unpaged()));
  }

  @Test
  void testGetAllSet_Success() {
    when(watchListRepository.findAll()).thenReturn(List.of(entity));
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    Set<WatchListResponse> result = watchListService.getAll();
    assertEquals(1, result.size());
  }

  @Test
  void testGetAllSet_Empty() {
    when(watchListRepository.findAll()).thenReturn(Collections.emptyList());
    Set<WatchListResponse> result = watchListService.getAll();
    assertTrue(result.isEmpty());
  }

  @Test
  void testUpdate_Success() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.of(entity));
    when(watchListRepository.save(entity)).thenReturn(entity);
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    WatchListResponse result = watchListService.update("id1", request);
    assertEquals(response, result);
    verify(watchListMapper).merge(request, entity);
  }

  @Test
  void testUpdate_NotFound() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> watchListService.update("id1", request));
  }

  @Test
  void testDelete_Success() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.of(entity));
    assertDoesNotThrow(() -> watchListService.delete("id1"));
    verify(watchListRepository).delete(entity);
  }

  @Test
  void testDelete_NotFound() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> watchListService.delete("id1"));
  }

  @Test
  void testPatch_Success() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.of(entity));
    when(watchListMapper.entityToResponse(entity)).thenReturn(response);

    WatchListResponse result = watchListService.patch("id1", request);
    assertEquals(response, result);
    verify(watchListMapper).merge(request, entity);
    verify(watchListRepository).save(entity);
  }

  @Test
  void testPatch_NotFound() {
    when(watchListRepository.findById("id1")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> watchListService.patch("id1", request));
  }
}
