package com.tragepro.api.datafeed.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

  @Bean(name = "customSchedulerExecutor")
  public Executor customSchedulerExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }

  @Bean(name = "datafeedThreadPoolExecutor")
  public Executor datafeedThreadPoolExecutor() {
    org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor executor =
        new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    executor.setThreadNamePrefix("datafeed-thread-");
    executor.initialize();
    return executor;
  }
}
