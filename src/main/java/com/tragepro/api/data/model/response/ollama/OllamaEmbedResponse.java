package com.tragepro.api.data.model.response.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model returned by the /api/embed endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaEmbedResponse {
    private String model;
    private List<List<Double>> embeddings;
    private Long totalDuration;
    private Long loadDuration;
    private Integer promptEvalCount;
}
