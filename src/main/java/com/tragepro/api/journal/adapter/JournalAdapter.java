package com.tragepro.api.journal.adapter;

import com.tragepro.api.domain.journal.request.JournalRequest;
import com.tragepro.api.domain.journal.request.TradeFilter;
import com.tragepro.api.domain.journal.response.JournalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalAdapter {
    JournalResponse createJournal(JournalRequest request);

    JournalResponse getJournalById(String id);

    Page<JournalResponse> getAllJournals(TradeFilter filter, Pageable pageable);

    JournalResponse updateJournal(String id, JournalRequest request);
}
