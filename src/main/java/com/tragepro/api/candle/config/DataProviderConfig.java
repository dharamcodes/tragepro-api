package com.tragepro.api.candle.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(DataProviderProperties.class)
public class DataProviderConfig {

    @Bean
    public RestClient dataProviderRestClient() {
        return RestClient.create();
    }
}
