package com.tragepro.api.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "temporal")
public class TemporalProperties {
  private Boolean enabled;
  private ServerProperties server;
  private WorkerProperties worker;
  private ClientProperties client;
}
