package com.tragepro.api.data.model.response.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tragepro.api.data.model.request.ollama.OllamaMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model returned by the /api/chat endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaChatResponse {
    private String model;
    private String createdAt;
    private OllamaMessage message;
    private Boolean done;
    private Long totalDuration;
    private Long loadDuration;
    private Integer promptEvalCount;
    private Integer evalCount;
}
