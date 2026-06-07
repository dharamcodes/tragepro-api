package com.tragepro.api.data.client.rest;

import com.tragepro.api.data.client.rest.config.OllamaProperties;
import com.tragepro.api.data.model.request.ollama.OllamaChatRequest;
import com.tragepro.api.data.model.request.ollama.OllamaCopyRequest;
import com.tragepro.api.data.model.request.ollama.OllamaCreateRequest;
import com.tragepro.api.data.model.request.ollama.OllamaEmbedRequest;
import com.tragepro.api.data.model.request.ollama.OllamaGenerateRequest;
import com.tragepro.api.data.model.request.ollama.OllamaModelRequest;
import com.tragepro.api.data.model.response.ollama.OllamaChatResponse;
import com.tragepro.api.data.model.response.ollama.OllamaEmbedResponse;
import com.tragepro.api.data.model.response.ollama.OllamaGenerateResponse;
import com.tragepro.api.data.model.response.ollama.OllamaModelInfoResponse;
import com.tragepro.api.data.model.response.ollama.OllamaModelListResponse;
import com.tragepro.api.data.model.response.ollama.OllamaStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * REST API client for interacting with a local Ollama Small Language Model (SLM) server.
 */
@Slf4j
@Component
public class OllamaSlmClient {

    private final RestClient restClient;

    public OllamaSlmClient(RestClient.Builder restClientBuilder, OllamaProperties properties) {
        log.info("Initializing Ollama REST Client with base URL: {}", properties.getApiUrl());
        this.restClient = restClientBuilder
                .baseUrl(properties.getApiUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Generates a response for a given prompt completion.
     */
    public OllamaGenerateResponse generate(OllamaGenerateRequest request) {
        log.debug("Sending generate request to Ollama: {}", request);
        return restClient.post().uri("/api/generate").body(request).retrieve().body(OllamaGenerateResponse.class);
    }

    /**
     * Generates a chat response using conversation history context.
     */
    public OllamaChatResponse chat(OllamaChatRequest request) {
        log.debug("Sending chat request to Ollama: {}", request);
        return restClient.post().uri("/api/chat").body(request).retrieve().body(OllamaChatResponse.class);
    }

    /**
     * Generates vector embeddings for a given prompt input.
     */
    public OllamaEmbedResponse embed(OllamaEmbedRequest request) {
        log.debug("Sending embed request to Ollama: {}", request);
        return restClient.post().uri("/api/embed").body(request).retrieve().body(OllamaEmbedResponse.class);
    }

    /**
     * Lists models available locally on the Ollama server.
     */
    public OllamaModelListResponse listModels() {
        log.debug("Sending list local models request to Ollama");
        return restClient.get().uri("/api/tags").retrieve().body(OllamaModelListResponse.class);
    }

    /**
     * Shows detail/metadata about a specific model.
     */
    public OllamaModelInfoResponse showModel(OllamaModelRequest request) {
        log.debug("Sending show model details request to Ollama: {}", request);
        return restClient.post().uri("/api/show").body(request).retrieve().body(OllamaModelInfoResponse.class);
    }

    /**
     * Creates a new model from a Modelfile definition.
     */
    public OllamaStatusResponse createModel(OllamaCreateRequest request) {
        log.debug("Sending create model request to Ollama: {}", request);
        return restClient.post().uri("/api/create").body(request).retrieve().body(OllamaStatusResponse.class);
    }

    /**
     * Duplicates/copies an existing local model.
     */
    public void copyModel(OllamaCopyRequest request) {
        log.debug("Sending copy model request to Ollama: {}", request);
        restClient.post().uri("/api/copy").body(request).retrieve().toBodilessEntity();
    }

    /**
     * Deletes a model and all its local weights.
     */
    public void deleteModel(OllamaModelRequest request) {
        log.debug("Sending delete model request to Ollama: {}", request);
        restClient
                .method(HttpMethod.DELETE)
                .uri("/api/delete")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Downloads/pulls a model from the Ollama library.
     */
    public OllamaStatusResponse pullModel(OllamaModelRequest request) {
        log.debug("Sending pull model request to Ollama: {}", request);
        return restClient.post().uri("/api/pull").body(request).retrieve().body(OllamaStatusResponse.class);
    }

    /**
     * Uploads/pushes a model to a model library registry.
     */
    public OllamaStatusResponse pushModel(OllamaModelRequest request) {
        log.debug("Sending push model request to Ollama: {}", request);
        return restClient.post().uri("/api/push").body(request).retrieve().body(OllamaStatusResponse.class);
    }
}
