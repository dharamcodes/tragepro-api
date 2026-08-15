package com.tragepro.api.domain.datafeed;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "data.socket")
public class SocketConfig {
  private String url;
  private String token;
  private String clientId;
  private boolean reconnect;
  private String urlPattern;
  private int maxReconnectAttempts;
  private long initialReconnectDelay;
  private long maxReconnectDelay;
}
