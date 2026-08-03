package com.tragepro.api.strategy.internal.service;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.WorkflowRequest;
import com.tragepro.api.strategy.dto.WorkflowResponse;
import java.util.Set;

/** Domain service managing strategy configurations, creation, updates, and execution workflows. */
public interface StrategyService {

  /**
   * Creates a new trading strategy entry.
   *
   * @param strategyRequest strategy payload
   * @return strategy response
   */
  StrategyResponse create(StrategyRequest strategyRequest);

  /**
   * Retrieves a strategy by identifier.
   *
   * @param id strategy identifier
   * @return strategy response
   */
  StrategyResponse getById(String id);

  /**
   * Retrieves all defined strategies.
   *
   * @return set of strategy responses
   */
  Set<StrategyResponse> getAll();

  /**
   * Updates an existing strategy by identifier.
   *
   * @param id strategy identifier
   * @param strategyRequest updated payload
   * @return updated strategy response
   */
  StrategyResponse update(String id, StrategyRequest strategyRequest);

  /**
   * Creates a new strategy or updates existing strategy if matching watchlist and symbol exist.
   *
   * @param strategyRequest strategy payload
   * @return created or updated strategy response
   */
  StrategyResponse createOrUpdate(StrategyRequest strategyRequest);

  /**
   * Deletes a strategy by identifier.
   *
   * @param id strategy identifier
   */
  void delete(String id);

  /**
   * Executes a strategy workflow for a specified strategy identifier.
   *
   * @param workflowRequest workflow request payload
   * @return workflow execution response
   */
  WorkflowResponse run(WorkflowRequest workflowRequest);
}
