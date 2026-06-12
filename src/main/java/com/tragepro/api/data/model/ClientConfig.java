package com.tragepro.api.data.model;

import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "data.rest")
public class ClientConfig {
    private String name;
    private String url;
    private String token;
    private String clientId;
    private Set<Header> headers;
    private String authHeaderPrefix;
    private String clientIdHeader;
}
