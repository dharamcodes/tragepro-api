package com.tragepro.api.data.config;

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
}
