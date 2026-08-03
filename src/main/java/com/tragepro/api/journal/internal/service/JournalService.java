package com.tragepro.api.journal.internal.service;

import com.tragepro.api.journal.dto.JournalRequest;
import com.tragepro.api.journal.dto.JournalResponse;
import com.tragepro.api.journal.dto.TradeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service interface managing trading journal entry creation, updates, and filtering. */
public interface JournalService {

  /**
   * Creates a new journal entry for a executed trade.
   *
   * @param request payload containing trade details
   * @return response representing created journal entry
   */
  JournalResponse createJournal(JournalRequest request);

  /**
   * Retrieves a journal entry by its unique identifier.
   *
   * @param id journal entry identifier
   * @return journal response
   */
  JournalResponse getJournalById(String id);

  /**
   * Retrieves a paginated list of journal entries matching filter parameters.
   *
   * @param filter trade filter criteria
   * @param pageable pagination options
   * @return page of journal responses
   */
  Page<JournalResponse> getAllJournals(TradeFilter filter, Pageable pageable);

  /**
   * Updates an existing journal entry by identifier.
   *
   * @param id journal entry identifier
   * @param request updated payload
   * @return updated journal response
   */
  JournalResponse updateJournal(String id, JournalRequest request);
}
