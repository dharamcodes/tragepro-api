package com.tragepro.api.alert.internal.service;

import com.tragepro.api.alert.AlertEvent;

/** Domain service for processing and dispatching system and strategy alerts. */
public interface AlertService {

  /**
   * Processes an incoming alert event and dispatches notifications.
   *
   * @param event the alert payload containing event identifier and message
   */
  void processAlert(AlertEvent event);
}
