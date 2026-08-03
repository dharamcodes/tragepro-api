package com.tragepro.api.strategy.internal.web;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.WorkflowRequest;
import com.tragepro.api.strategy.dto.WorkflowResponse;
import com.tragepro.api.strategy.internal.service.StrategyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "6. StrategyController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/strategy")
public class StrategyController {

  private final StrategyService strategyService;

  @PostMapping
  public ResponseEntity<StrategyResponse> createStrategy(
      @Valid @RequestBody StrategyRequest request) {
    return ResponseEntity.ok(strategyService.create(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<StrategyResponse> getStrategyById(@PathVariable String id) {
    return ResponseEntity.ok(strategyService.getById(id));
  }

  @GetMapping
  public ResponseEntity<Set<StrategyResponse>> getAllStrategies() {
    return ResponseEntity.ok(strategyService.getAll());
  }

  @PutMapping("/{id}")
  public ResponseEntity<StrategyResponse> updateStrategy(
      @PathVariable String id, @Valid @RequestBody StrategyRequest request) {
    return ResponseEntity.ok(strategyService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStrategy(@PathVariable String id) {
    strategyService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/run")
  public ResponseEntity<WorkflowResponse> runStrategy(@Valid @RequestBody WorkflowRequest request) {
    return ResponseEntity.ok(strategyService.run(request));
  }
}
