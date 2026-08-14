package com.tragepro.api.journal.internal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.journal.internal.mapper.JournalMapper;
import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.enums.TradeStatus;
import com.tragepro.api.journal.model.enums.TradeType;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.model.request.TradeFilter;
import com.tragepro.api.journal.model.response.JournalResponse;
import com.tragepro.api.journal.repository.JournalRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class JournalServiceImplTest {

  @Mock private JournalRepository journalRepository;

  @Mock private JournalMapper journalMapper;

  @InjectMocks private JournalServiceImpl journalService;

  private JournalRequest request;
  private JournalEntity entity;
  private JournalResponse response;
  private final String testId = "journalId";
  private final String accountId = "accId";

  @BeforeEach
  void setUp() {
    request =
        JournalRequest.builder()
            .accountId(accountId)
            .symbol("AAPL")
            .tradeType(TradeType.LONG)
            .status(TradeStatus.OPEN)
            .entryPrice(BigDecimal.valueOf(150))
            .quantity(BigDecimal.valueOf(10))
            .entryTime(Instant.now())
            .build();

    entity =
        JournalEntity.builder()
            .id(testId)
            .accountId(accountId)
            .symbol("AAPL")
            .tradeType(TradeType.LONG)
            .status(TradeStatus.OPEN)
            .entryPrice(BigDecimal.valueOf(150))
            .quantity(BigDecimal.valueOf(10))
            .entryTime(Instant.now())
            .build();

    response =
        JournalResponse.builder()
            .id(testId)
            .accountId(accountId)
            .symbol("AAPL")
            .tradeType(TradeType.LONG)
            .status(TradeStatus.OPEN)
            .entryPrice(BigDecimal.valueOf(150))
            .quantity(BigDecimal.valueOf(10))
            .entryTime(Instant.now())
            .build();
  }

  @Test
  void testCreateJournal() {
    when(journalMapper.toEntity(request)).thenReturn(entity);
    when(journalRepository.save(entity)).thenReturn(entity);
    when(journalMapper.toResponse(entity)).thenReturn(response);

    JournalResponse result = journalService.createJournal(request);

    assertNotNull(result);
    assertEquals(testId, result.getId());
    assertEquals("AAPL", result.getSymbol());
    verify(journalRepository, times(1)).save(entity);
  }

  @Test
  void testGetJournalById_Success() {
    when(journalRepository.findById(testId)).thenReturn(Optional.of(entity));
    when(journalMapper.toResponse(entity)).thenReturn(response);

    JournalResponse result = journalService.getJournalById(testId);

    assertNotNull(result);
    assertEquals(testId, result.getId());
    verify(journalRepository, times(1)).findById(testId);
  }

  @Test
  void testGetJournalById_NotFound() {
    when(journalRepository.findById(testId)).thenReturn(Optional.empty());

    AppException ex = assertThrows(AppException.class, () -> journalService.getJournalById(testId));
    assertEquals(ErrorType.DATA_NOT_FOUND, ex.getErrorType());
    verify(journalRepository, times(1)).findById(testId);
  }

  @Test
  void testGetAllJournals() {
    TradeFilter filter = TradeFilter.builder().accountId(accountId).build();
    Pageable pageable = PageRequest.of(0, 10);
    Page<JournalEntity> page = new PageImpl<>(List.of(entity));

    when(journalRepository.findWithFilters(filter, pageable)).thenReturn(page);
    when(journalMapper.toResponse(entity)).thenReturn(response);

    Page<JournalResponse> resultPage = journalService.getAllJournals(filter, pageable);

    assertNotNull(resultPage);
    assertEquals(1, resultPage.getTotalElements());
    assertEquals(response, resultPage.getContent().get(0));
    verify(journalRepository, times(1)).findWithFilters(filter, pageable);
  }

  @Test
  void testUpdateJournal_Success() {
    when(journalRepository.findById(testId)).thenReturn(Optional.of(entity));
    doNothing().when(journalMapper).updateEntityFromRequest(request, entity);
    when(journalRepository.save(entity)).thenReturn(entity);
    when(journalMapper.toResponse(entity)).thenReturn(response);

    JournalResponse result = journalService.updateJournal(testId, request);

    assertNotNull(result);
    assertEquals(testId, result.getId());
    verify(journalRepository, times(1)).findById(testId);
    verify(journalMapper, times(1)).updateEntityFromRequest(request, entity);
    verify(journalRepository, times(1)).save(entity);
  }

  @Test
  void testUpdateJournal_NotFound() {
    when(journalRepository.findById(testId)).thenReturn(Optional.empty());

    AppException ex =
        assertThrows(AppException.class, () -> journalService.updateJournal(testId, request));
    assertEquals(ErrorType.DATA_NOT_FOUND, ex.getErrorType());
    verify(journalRepository, times(1)).findById(testId);
    verify(journalRepository, never()).save(any());
  }
}
