package com.tragepro.api.data.model.request.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body model for /api/generate endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaGenerateRequest {
    private String model;
    private String prompt;
    private String system;
    private String template;
    private int[] context;

    @Builder.Default
    private Boolean stream = false;

    private Object format;
    private Map<String, Object> options;
    private String keepAlive;
}
