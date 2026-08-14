package com.tragepro.api.journal.internal;

import com.tragepro.api.common.model.response.PagedResponse;
import com.tragepro.api.journal.JournalAdapter;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.model.request.TradeFilter;
import com.tragepro.api.journal.model.response.JournalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class JournalAdapterImpl implements JournalAdapter {

  private final JournalService journalService;

  @Override
  public JournalResponse logTrade(JournalRequest request) {
    return journalService.createJournal(request);
  }

  @Override
  public PagedResponse<JournalResponse> getJournals(TradeFilter filter, Pageable pageable) {
    Page<JournalResponse> journalsPage = journalService.getAllJournals(filter, pageable);
    return PagedResponse.of(journalsPage);
  }

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
