package com.tragepro.api.data.model.response.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model returned by the /api/show endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaModelInfoResponse {
    private String license;
    private String modelfile;
    private String parameters;
    private String template;
    private OllamaModelDetails details;
    private Map<String, Object> modelInfo;
}
