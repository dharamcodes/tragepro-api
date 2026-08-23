package com.tragepro.api.journal.web;

import com.tragepro.api.domain.journal.request.JournalRequest;
import com.tragepro.api.domain.journal.request.TradeFilter;
import com.tragepro.api.domain.journal.response.JournalResponse;
import com.tragepro.api.journal.service.JournalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "4. Trade Journal & Analytics", description = "Trading journals, logs, and performance filtering")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/journal")
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalResponse> createJournal(@Valid @RequestBody JournalRequest request) {
        return ResponseEntity.ok(journalService.createJournal(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable String id) {
        return ResponseEntity.ok(journalService.getJournalById(id));
    }

    @GetMapping
    public ResponseEntity<Page<JournalResponse>> getAllJournals(
            @ModelAttribute TradeFilter filter,
            @PageableDefault(sort = "entryTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(journalService.getAllJournals(filter, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(
            @PathVariable String id, @Valid @RequestBody JournalRequest request) {
        return ResponseEntity.ok(journalService.updateJournal(id, request));
    }
}
