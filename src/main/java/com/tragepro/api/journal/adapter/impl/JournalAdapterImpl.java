package com.tragepro.api.journal.adapter.impl;

import com.tragepro.api.domain.journal.request.JournalRequest;
import com.tragepro.api.domain.journal.request.TradeFilter;
import com.tragepro.api.domain.journal.response.JournalResponse;
import com.tragepro.api.journal.adapter.JournalAdapter;
import com.tragepro.api.journal.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JournalAdapterImpl implements JournalAdapter {
    private final JournalService journalService;

    @Override
    public JournalResponse createJournal(JournalRequest request) {
        return journalService.createJournal(request);
    }

    @Override
    public JournalResponse getJournalById(String id) {
        return journalService.getJournalById(id);
    }

    @Override
    public Page<JournalResponse> getAllJournals(TradeFilter filter, Pageable pageable) {
        return journalService.getAllJournals(filter, pageable);
    }

    @Override
    public JournalResponse updateJournal(String id, JournalRequest request) {
        return journalService.updateJournal(id, request);
    }
}
