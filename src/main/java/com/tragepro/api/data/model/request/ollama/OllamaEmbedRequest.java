package com.tragepro.api.data.model.request.ollama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body model for /api/embed endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaEmbedRequest {
    private String model;
    private Object input; // Can be String or List<String>
    private Boolean truncate;
    private Map<String, Object> options;
    private String keepAlive;
}
