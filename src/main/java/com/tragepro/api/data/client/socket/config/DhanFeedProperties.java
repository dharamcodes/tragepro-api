package com.tragepro.api.data.client.socket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties mapped to "feed.vendors.dhanhq" in properties/YML configuration files.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "feed.vendors.dhanhq")
public class DhanFeedProperties {
    private String clientId;
    private String accessToken;
    private String restUrl;
    private String websocketUrl;
}
