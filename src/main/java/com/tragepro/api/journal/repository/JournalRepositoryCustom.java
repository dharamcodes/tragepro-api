package com.tragepro.api.journal.repository;

import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.request.TradeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalRepositoryCustom {
  Page<JournalEntity> findWithFilters(TradeFilter filter, Pageable pageable);
}
