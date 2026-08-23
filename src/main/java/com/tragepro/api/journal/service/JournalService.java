package com.tragepro.api.journal.service;

import com.tragepro.api.domain.journal.request.JournalRequest;
import com.tragepro.api.domain.journal.request.TradeFilter;
import com.tragepro.api.domain.journal.response.JournalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalService {

    JournalResponse createJournal(JournalRequest request);

    JournalResponse getJournalById(String id);

    Page<JournalResponse> getAllJournals(TradeFilter filter, Pageable pageable);

    JournalResponse updateJournal(String id, JournalRequest request);
}
