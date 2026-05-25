package com.tragepro.api.candle.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "data-provider")
public record DataProviderProperties(Map<String, ProviderConfig> clients, String fetchCron, String interval) {
    public record ProviderConfig(
            @NotBlank String baseUrl,
            @NotBlank String dataUrl,
            String apiKey,
            String authToken,
            @NotEmpty List<String> symbols,
            @Positive int timeoutSeconds,
            @Positive int batchSize,
            String method,
            String body) {}
}
