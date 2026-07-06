package com.tragepro.api.datafeed.config;

import com.tragepro.api.common.props.ClientConfig;
import com.tragepro.api.datafeed.client.FeedClient;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HttpExchangeConfig {

  private final ClientConfig clientConfig;

  @Bean
  public FeedClient dataFeedClient() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(clientConfig.getConTimeout());
    requestFactory.setReadTimeout(clientConfig.getReadTimeout());
    RestClient.Builder builder = RestClient.builder().requestFactory(requestFactory);
    builder.baseUrl(clientConfig.getBaseUrl());
    Optional.of(clientConfig.getClientHeaders())
        .orElse(Set.of())
        .forEach(header -> builder.defaultHeader(header.getName(), header.getValue()));
    RestClient restClient = builder.build();
    HttpServiceProxyFactory factory =
        HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
    log.info("Client configuration completed for clientName :: {}", clientConfig.getClientName());
    return factory.createClient(FeedClient.class);
  }
}
