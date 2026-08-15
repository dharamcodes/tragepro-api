package com.tragepro.api.journal.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.journal.entity.JournalEntity;
import com.tragepro.api.domain.journal.enums.TradeType;
import com.tragepro.api.domain.journal.request.TradeFilter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@ExtendWith(MockitoExtension.class)
class JournalRepositoryImplTest {

  @Mock private MongoTemplate mongoTemplate;

  @InjectMocks private JournalRepositoryImpl journalRepositoryImpl;

  @Captor private ArgumentCaptor<Query> queryCaptor;

  private Pageable pageable;
  private JournalEntity entity;

  @BeforeEach
  void setUp() {
    pageable = PageRequest.of(0, 10);
    entity = new JournalEntity();
    entity.setId("testId");
  }

  @Test
  void testFindWithFilters_NoFilters() {
    TradeFilter filter = TradeFilter.builder().build();

    when(mongoTemplate.count(any(Query.class), eq(JournalEntity.class))).thenReturn(1L);
    when(mongoTemplate.find(any(Query.class), eq(JournalEntity.class))).thenReturn(List.of(entity));

    Page<JournalEntity> result = journalRepositoryImpl.findWithFilters(filter, pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());

    verify(mongoTemplate).count(queryCaptor.capture(), eq(JournalEntity.class));
    Query query = queryCaptor.getValue();
    assertEquals(0, query.getQueryObject().size());
  }

  @Test
  void testFindWithFilters_WithBasicFilters() {
    TradeFilter filter =
        TradeFilter.builder().accountId("accId").symbol("AAPL").tradeType(TradeType.LONG).build();

    when(mongoTemplate.count(any(Query.class), eq(JournalEntity.class))).thenReturn(1L);
    when(mongoTemplate.find(any(Query.class), eq(JournalEntity.class))).thenReturn(List.of(entity));

    Page<JournalEntity> result = journalRepositoryImpl.findWithFilters(filter, pageable);

    assertNotNull(result);

    verify(mongoTemplate).count(queryCaptor.capture(), eq(JournalEntity.class));
    Query query = queryCaptor.getValue();
    assertEquals("accId", query.getQueryObject().get("accountId"));
    assertEquals("AAPL", query.getQueryObject().get("symbol"));
    assertEquals(TradeType.LONG, query.getQueryObject().get("tradeType"));
  }

  @Test
  void testFindWithFilters_WithYearFilter() {
    TradeFilter filter = TradeFilter.builder().year(2023).build();

    when(mongoTemplate.count(any(Query.class), eq(JournalEntity.class))).thenReturn(0L);
    when(mongoTemplate.find(any(Query.class), eq(JournalEntity.class))).thenReturn(List.of());

    journalRepositoryImpl.findWithFilters(filter, pageable);

    verify(mongoTemplate).count(queryCaptor.capture(), eq(JournalEntity.class));
    Query query = queryCaptor.getValue();
    assertNotNull(query.getQueryObject().get("entryTime"));
  }

  @Test
  void testFindWithFilters_WithYearMonthFilter() {
    TradeFilter filter = TradeFilter.builder().year(2023).month(5).build();

    when(mongoTemplate.count(any(Query.class), eq(JournalEntity.class))).thenReturn(0L);
    when(mongoTemplate.find(any(Query.class), eq(JournalEntity.class))).thenReturn(List.of());

    journalRepositoryImpl.findWithFilters(filter, pageable);

    verify(mongoTemplate).count(queryCaptor.capture(), eq(JournalEntity.class));
    Query query = queryCaptor.getValue();
    assertNotNull(query.getQueryObject().get("entryTime"));
  }

  @Test
  void testFindWithFilters_WithYearMonthDayFilter() {
    TradeFilter filter = TradeFilter.builder().year(2023).month(5).day(15).build();

    when(mongoTemplate.count(any(Query.class), eq(JournalEntity.class))).thenReturn(0L);
    when(mongoTemplate.find(any(Query.class), eq(JournalEntity.class))).thenReturn(List.of());

    journalRepositoryImpl.findWithFilters(filter, pageable);

    verify(mongoTemplate).count(queryCaptor.capture(), eq(JournalEntity.class));
    Query query = queryCaptor.getValue();
    assertNotNull(query.getQueryObject().get("entryTime"));
  }
}
