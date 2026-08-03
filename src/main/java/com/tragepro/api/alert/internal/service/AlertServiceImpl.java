package com.tragepro.api.alert.internal.service;

import com.tragepro.api.alert.AlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service implementation for handling alert notifications with logging. */
@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

  /**
   * Processes an incoming alert event and logs the alert payload details.
   *
   * @param event the alert payload containing event identifier and message
   */
  @Override
  public void processAlert(AlertEvent event) {
    log.info("Processing alert event: id={}, message={}", event.eventId(), event.message());
  }
}
