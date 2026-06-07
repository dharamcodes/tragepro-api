package com.tragepro.api.data.model.request.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body model for /api/chat endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaChatRequest {
    private String model;
    private List<OllamaMessage> messages;

    @Builder.Default
    private Boolean stream = false;

    private Object format;
    private Map<String, Object> options;
    private String keepAlive;
}
