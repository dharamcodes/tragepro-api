package com.tragepro.api.common.properties;

import lombok.Data;

@Data
public class WorkerProperties {
  private String taskQueue;
  private int maxConcurrentWorkflowTaskPollers;
  private int maxConcurrentActivityTaskPollers;
  private int stickyCacheSize;
  private int activityStartToCloseTimeoutSeconds = 60;
}
