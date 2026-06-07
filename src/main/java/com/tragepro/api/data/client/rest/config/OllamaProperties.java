package com.tragepro.api.data.client.rest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties mapped to "ollama" in properties/YML configuration files.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ollama")
public class OllamaProperties {
    private String apiUrl = "http://localhost:11434";
}
