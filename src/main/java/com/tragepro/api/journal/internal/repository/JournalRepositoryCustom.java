package com.tragepro.api.journal.internal.repository;

import com.tragepro.api.journal.dto.TradeFilter;
import com.tragepro.api.journal.internal.entity.JournalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalRepositoryCustom {
  Page<JournalEntity> findWithFilters(TradeFilter filter, Pageable pageable);
}
