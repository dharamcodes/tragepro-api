package com.tragepro.api.common;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mongodb.MongoDBContainer;

public class ContainerConfig {

  @Container @ServiceConnection
  protected static MongoDBContainer mongo =
      new MongoDBContainer("mongo:latest").withReplicaSet().withReuse(true);

  protected static WireMockServer wireMockServer;

  @BeforeAll
  static void init() {
    mongo.start();
    if (wireMockServer == null) {
      wireMockServer =
          new WireMockServer(
              WireMockConfiguration.wireMockConfig()
                  .dynamicPort()
                  .withRootDirectory("src/test/resources"));
      wireMockServer.start();

      wireMockServer.stubFor(
          post(urlPathMatching("/charts/.*"))
              .atPriority(1)
              .willReturn(
                  aResponse()
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("feed_client_response.json")
                      .withStatus(200)));

      wireMockServer.stubFor(
          post(urlPathMatching(".*"))
              .atPriority(10)
              .willReturn(
                  aResponse()
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("historical_candles_response.json")
                      .withStatus(200)));

      wireMockServer.stubFor(
          get(urlPathMatching(".*"))
              .atPriority(10)
              .willReturn(
                  aResponse()
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("security_response.json")
                      .withStatus(200)));
    }
  }

  @DynamicPropertySource
  static void configureWireMockPort(DynamicPropertyRegistry registry) {
    if (wireMockServer != null && wireMockServer.isRunning()) {
      registry.add("data.rest.baseUrl", () -> wireMockServer.baseUrl());
    }
  }
}
