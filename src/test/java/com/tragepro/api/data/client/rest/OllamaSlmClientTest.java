package com.tragepro.api.data.client.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.data.client.rest.config.OllamaProperties;
import com.tragepro.api.data.model.request.ollama.OllamaChatRequest;
import com.tragepro.api.data.model.request.ollama.OllamaCopyRequest;
import com.tragepro.api.data.model.request.ollama.OllamaCreateRequest;
import com.tragepro.api.data.model.request.ollama.OllamaEmbedRequest;
import com.tragepro.api.data.model.request.ollama.OllamaGenerateRequest;
import com.tragepro.api.data.model.request.ollama.OllamaMessage;
import com.tragepro.api.data.model.request.ollama.OllamaModelRequest;
import com.tragepro.api.data.model.response.ollama.OllamaChatResponse;
import com.tragepro.api.data.model.response.ollama.OllamaEmbedResponse;
import com.tragepro.api.data.model.response.ollama.OllamaGenerateResponse;
import com.tragepro.api.data.model.response.ollama.OllamaModelInfoResponse;
import com.tragepro.api.data.model.response.ollama.OllamaModelListResponse;
import com.tragepro.api.data.model.response.ollama.OllamaStatusResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class OllamaSlmClientTest {

    private OllamaSlmClient ollamaSlmClient;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        OllamaProperties properties = new OllamaProperties();
        properties.setApiUrl("http://localhost:11434");

        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        ollamaSlmClient = new OllamaSlmClient(builder, properties);
    }

    @Test
    void testGenerate() throws Exception {
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .model("llama3.2")
                .prompt("Why is the sky blue?")
                .build();

        OllamaGenerateResponse expectedResponse = OllamaGenerateResponse.builder()
                .model("llama3.2")
                .response("Rayleigh scattering")
                .done(true)
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/generate"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(request)))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaGenerateResponse response = ollamaSlmClient.generate(request);
        assertNotNull(response);
        assertEquals("llama3.2", response.getModel());
        assertEquals("Rayleigh scattering", response.getResponse());
        assertTrue(response.getDone());
        mockServer.verify();
    }

    @Test
    void testChat() throws Exception {
        OllamaChatRequest request = OllamaChatRequest.builder()
                .model("llama3.2")
                .messages(Collections.singletonList(
                        OllamaMessage.builder().role("user").content("Hello").build()))
                .build();

        OllamaChatResponse expectedResponse = OllamaChatResponse.builder()
                .model("llama3.2")
                .message(OllamaMessage.builder()
                        .role("assistant")
                        .content("Hi there!")
                        .build())
                .done(true)
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/chat"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaChatResponse response = ollamaSlmClient.chat(request);
        assertNotNull(response);
        assertEquals("llama3.2", response.getModel());
        assertEquals("assistant", response.getMessage().getRole());
        assertEquals("Hi there!", response.getMessage().getContent());
        mockServer.verify();
    }

    @Test
    void testEmbed() throws Exception {
        OllamaEmbedRequest request = OllamaEmbedRequest.builder()
                .model("nomic-embed-text")
                .input("Hello world")
                .build();

        OllamaEmbedResponse expectedResponse = OllamaEmbedResponse.builder()
                .model("nomic-embed-text")
                .embeddings(Collections.singletonList(List.of(0.1, -0.2, 0.3)))
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/embed"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaEmbedResponse response = ollamaSlmClient.embed(request);
        assertNotNull(response);
        assertEquals("nomic-embed-text", response.getModel());
        assertEquals(1, response.getEmbeddings().size());
        assertEquals(0.1, response.getEmbeddings().get(0).get(0));
        mockServer.verify();
    }

    @Test
    void testListModels() throws Exception {
        OllamaModelListResponse expectedResponse = OllamaModelListResponse.builder()
                .models(Collections.emptyList())
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/tags"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaModelListResponse response = ollamaSlmClient.listModels();
        assertNotNull(response);
        assertTrue(response.getModels().isEmpty());
        mockServer.verify();
    }

    @Test
    void testShowModel() throws Exception {
        OllamaModelRequest request =
                OllamaModelRequest.builder().model("llama3.2").build();
        OllamaModelInfoResponse expectedResponse = OllamaModelInfoResponse.builder()
                .modelfile("# Modelfile")
                .parameters("stop [\"\\n\"]")
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/show"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaModelInfoResponse response = ollamaSlmClient.showModel(request);
        assertNotNull(response);
        assertEquals("# Modelfile", response.getModelfile());
        assertEquals("stop [\"\\n\"]", response.getParameters());
        mockServer.verify();
    }

    @Test
    void testCreateModel() throws Exception {
        OllamaCreateRequest request = OllamaCreateRequest.builder()
                .model("custom-model")
                .modelfile("FROM llama3.2")
                .build();

        OllamaStatusResponse expectedResponse =
                OllamaStatusResponse.builder().status("success").build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/create"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaStatusResponse response = ollamaSlmClient.createModel(request);
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        mockServer.verify();
    }

    @Test
    void testCopyModel() throws Exception {
        OllamaCopyRequest request = OllamaCopyRequest.builder()
                .source("llama3.2")
                .destination("llama3.2-copy")
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/copy"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess());

        assertDoesNotThrow(() -> ollamaSlmClient.copyModel(request));
        mockServer.verify();
    }

    @Test
    void testDeleteModel() throws Exception {
        OllamaModelRequest request =
                OllamaModelRequest.builder().model("llama3.2-copy").build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/delete"))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess());

        assertDoesNotThrow(() -> ollamaSlmClient.deleteModel(request));
        mockServer.verify();
    }

    @Test
    void testPullModel() throws Exception {
        OllamaModelRequest request =
                OllamaModelRequest.builder().model("llama3.2").build();
        OllamaStatusResponse expectedResponse = OllamaStatusResponse.builder()
                .status("downloading")
                .completed(50L)
                .total(100L)
                .build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/pull"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaStatusResponse response = ollamaSlmClient.pullModel(request);
        assertNotNull(response);
        assertEquals("downloading", response.getStatus());
        assertEquals(50L, response.getCompleted());
        assertEquals(100L, response.getTotal());
        mockServer.verify();
    }

    @Test
    void testPushModel() throws Exception {
        OllamaModelRequest request =
                OllamaModelRequest.builder().model("custom-model").build();
        OllamaStatusResponse expectedResponse =
                OllamaStatusResponse.builder().status("uploading").build();

        mockServer
                .expect(requestTo("http://localhost:11434/api/push"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        OllamaStatusResponse response = ollamaSlmClient.pushModel(request);
        assertNotNull(response);
        assertEquals("uploading", response.getStatus());
        mockServer.verify();
    }
}
