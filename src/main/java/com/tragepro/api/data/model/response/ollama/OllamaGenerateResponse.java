package com.tragepro.api.data.model.response.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model returned by the /api/generate endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaGenerateResponse {
    private String model;
    private String createdAt;
    private String response;
    private Boolean done;
    private int[] context;
    private Long totalDuration;
    private Long loadDuration;
    private Integer promptEvalCount;
    private Integer evalCount;
}
