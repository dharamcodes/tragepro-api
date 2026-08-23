package com.tragepro.api.journal.core.repository;

import com.tragepro.api.domain.journal.entity.JournalEntity;
import com.tragepro.api.domain.journal.request.TradeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalRepositoryCustom {
    Page<JournalEntity> findWithFilters(TradeFilter filter, Pageable pageable);
}
