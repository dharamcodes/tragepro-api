package com.tragepro.api.journal.repository;

import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.request.TradeFilter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class JournalRepositoryImpl implements JournalRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Override
  public Page<JournalEntity> findWithFilters(TradeFilter filter, Pageable pageable) {
    Query query = new Query();

    if (filter.getAccountId() != null && !filter.getAccountId().isBlank()) {
      query.addCriteria(Criteria.where("accountId").is(filter.getAccountId()));
    }
    if (filter.getSymbol() != null && !filter.getSymbol().isBlank()) {
      query.addCriteria(Criteria.where("symbol").is(filter.getSymbol()));
    }
    if (filter.getTradeType() != null) {
      query.addCriteria(Criteria.where("tradeType").is(filter.getTradeType()));
    }

    if (filter.getYear() != null) {
      Instant start;
      Instant end;
      if (filter.getMonth() != null) {
        if (filter.getDay() != null) {
          LocalDate date = LocalDate.of(filter.getYear(), filter.getMonth(), filter.getDay());
          start = date.atStartOfDay().toInstant(ZoneOffset.UTC);
          end = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        } else {
          YearMonth ym = YearMonth.of(filter.getYear(), filter.getMonth());
          start = ym.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
          end = ym.plusMonths(1).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        }
      } else {
        LocalDate date = LocalDate.of(filter.getYear(), 1, 1);
        start = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        end = date.plusYears(1).atStartOfDay().toInstant(ZoneOffset.UTC);
      }
      query.addCriteria(Criteria.where("entryTime").gte(start).lt(end));
    }

    long total = mongoTemplate.count(query, JournalEntity.class);
    query.with(pageable);
    List<JournalEntity> content = mongoTemplate.find(query, JournalEntity.class);

    return new PageImpl<>(content, pageable, total);
  }
}
