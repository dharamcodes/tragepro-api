package com.tragepro.api.journal.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.model.response.PagedResponse;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.model.request.TradeFilter;
import com.tragepro.api.journal.model.response.JournalResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class JournalAdapterTest {

  @Mock private JournalService journalService;

  private JournalAdapterImpl journalAdapter;

  @BeforeEach
  void setUp() {
    journalAdapter = new JournalAdapterImpl(journalService);
  }

  @Test
  void testLogTrade() {
    JournalRequest request = JournalRequest.builder().notes("Good trade").build();
    JournalResponse expectedResponse =
        JournalResponse.builder().id("j-1").notes("Good trade").build();
    when(journalService.createJournal(request)).thenReturn(expectedResponse);

    JournalResponse response = journalAdapter.logTrade(request);

    assertNotNull(response);
    assertEquals("j-1", response.getId());
    verify(journalService).createJournal(request);
  }

  @Test
  void testGetJournals() {
    TradeFilter filter = new TradeFilter("AAPL", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);
    JournalResponse item = JournalResponse.builder().id("j-1").symbol("AAPL").build();
    PageImpl<JournalResponse> page = new PageImpl<>(List.of(item), pageable, 1);

    when(journalService.getAllJournals(filter, pageable)).thenReturn(page);

    PagedResponse<JournalResponse> response = journalAdapter.getJournals(filter, pageable);

    assertNotNull(response);
    assertEquals(1, response.content().size());
    assertEquals("j-1", response.content().get(0).getId());
    verify(journalService).getAllJournals(filter, pageable);
  }
}
