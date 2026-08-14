package com.tragepro.api;

import com.tragepro.api.core.ContainerConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@Slf4j
@SpringBootTest
class ApplicationTest extends ContainerConfig {

  @Test
  void contextLoads() {
    ApplicationModules modules = ApplicationModules.of(Application.class);
    modules.forEach(module -> log.debug(module.getDisplayName()));
    modules.verify();
  }
}
