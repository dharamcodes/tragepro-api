package com.tragepro.api.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "datafeedExecutor")
    public Executor datafeedExecutor() {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("datafeed-async-", 1).factory());
    }
}
