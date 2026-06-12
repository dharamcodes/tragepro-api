package com.tragepro.api.data.config;

import com.tragepro.api.data.client.DataFeedClient;
import com.tragepro.api.data.model.ClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpExchangeConfig {

    private final ClientConfig clientConfig;

    @Bean
    public DataFeedClient dataFeedClient() {
        RestClient.Builder builder = RestClient.builder();
        builder.baseUrl(clientConfig.getUrl());

        String authHeaderPrefix = clientConfig.getAuthHeaderPrefix();
        if (authHeaderPrefix == null) {
            authHeaderPrefix = "";
        }
        builder.defaultHeader(HttpHeaders.AUTHORIZATION, authHeaderPrefix + clientConfig.getToken());

        String clientIdHeader = clientConfig.getClientIdHeader();
        if (clientIdHeader != null && !clientIdHeader.isEmpty()) {
            builder.defaultHeader(clientIdHeader, clientConfig.getClientId());
        }
        if (clientConfig.getHeaders() != null) {
            clientConfig.getHeaders().stream().forEach(h -> builder.defaultHeader(h.getName(), h.getValue()));
        }
        RestClient restClient = builder.build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(DataFeedClient.class);
    }
}
