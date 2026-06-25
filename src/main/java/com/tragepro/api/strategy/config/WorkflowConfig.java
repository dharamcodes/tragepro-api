package com.tragepro.api.strategy.config;

import java.io.File;
import java.util.Collections;
import org.copperengine.core.common.DefaultProcessorPoolManager;
import org.copperengine.core.common.DefaultTicketPoolManager;
import org.copperengine.core.common.JdkRandomUUIDFactory;
import org.copperengine.core.tranzient.DefaultEarlyResponseContainer;
import org.copperengine.core.tranzient.DefaultTimeoutManager;
import org.copperengine.core.tranzient.TransientPriorityProcessorPool;
import org.copperengine.core.tranzient.TransientProcessorPool;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.copperengine.core.wfrepo.FileBasedWorkflowRepository;
import org.copperengine.spring.SpringDependencyInjector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfig {

  @Bean
  public SpringDependencyInjector springDependencyInjector() {
    return new SpringDependencyInjector();
  }

  @Bean(initMethod = "start", destroyMethod = "shutdown")
  public FileBasedWorkflowRepository fileBasedWorkflowRepository() {
    FileBasedWorkflowRepository repository = new FileBasedWorkflowRepository();
    repository.setSourceDirs(
        Collections.singletonList("src/main/java/com/tragepro/api/strategy/workflow"));
    File targetDir = new File("build/classes/java/main/com/tragepro/api/strategy/workflow");
    repository.setTargetDir(targetDir.getAbsolutePath());
    repository.setCompilerOptions("-g", "-d", targetDir.getAbsolutePath(), "-proc:none");
    return repository;
  }

  @Bean(initMethod = "startup", destroyMethod = "shutdown")
  public TransientScottyEngine copperEngine(
      FileBasedWorkflowRepository repository,
      SpringDependencyInjector dependencyInjector,
      @Value("${copper.engine.thread-count:4}") int threadCount) {
    TransientScottyEngine engine = new TransientScottyEngine();
    engine.setWfRepository(repository);
    engine.setDependencyInjector(dependencyInjector);
    engine.setIdFactory(new JdkRandomUUIDFactory());
    DefaultTimeoutManager timeoutManager = new DefaultTimeoutManager();
    timeoutManager.setEngine(engine);
    engine.setTimeoutManager(timeoutManager);
    DefaultEarlyResponseContainer earlyResponseContainer = new DefaultEarlyResponseContainer();
    engine.setEarlyResponseContainer(earlyResponseContainer);
    DefaultTicketPoolManager ticketPoolManager = new DefaultTicketPoolManager();
    engine.setTicketPoolManager(ticketPoolManager);
    DefaultProcessorPoolManager<TransientProcessorPool> poolManager =
        new DefaultProcessorPoolManager<>();
    poolManager.setEngine(engine);
    TransientPriorityProcessorPool pool =
        new TransientPriorityProcessorPool(
            TransientPriorityProcessorPool.DEFAULT_POOL_ID, threadCount);
    poolManager.setProcessorPools(Collections.singletonList(pool));
    engine.setPoolManager(poolManager);
    return engine;
  }
}
