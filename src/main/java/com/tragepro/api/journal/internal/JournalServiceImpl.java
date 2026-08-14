package com.tragepro.api.journal.internal;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.model.request.TradeFilter;
import com.tragepro.api.journal.model.response.JournalResponse;
import com.tragepro.api.journal.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class JournalServiceImpl implements JournalService {

  private final JournalRepository journalRepository;
  private final JournalMapper journalMapper;

  @Override
  public JournalResponse createJournal(JournalRequest request) {
    log.info("Creating new trade journal for account: {}", request.getAccountId());
    JournalEntity entity = journalMapper.toEntity(request);
    JournalEntity saved = journalRepository.save(entity);
    return journalMapper.toResponse(saved);
  }

  @Override
  public JournalResponse getJournalById(String id) {
    log.info("Fetching trade journal with id: {}", id);
    JournalEntity entity = findJournalById(id);
    return journalMapper.toResponse(entity);
  }

  @Override
  public Page<JournalResponse> getAllJournals(TradeFilter filter, Pageable pageable) {
    log.info("Fetching trade journals with filter: {}", filter);
    Page<JournalEntity> journalPage = journalRepository.findWithFilters(filter, pageable);
    return journalPage.map(journalMapper::toResponse);
  }

  @Override
  public JournalResponse updateJournal(String id, JournalRequest request) {
    log.info("Updating trade journal with id: {}", id);
    JournalEntity entity = findJournalById(id);
    journalMapper.updateEntityFromRequest(request, entity);
    JournalEntity updated = journalRepository.save(entity);
    return journalMapper.toResponse(updated);
  }

  private JournalEntity findJournalById(String id) {
    return journalRepository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
  }
}
