package com.tragepro.api;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithArchitectureTest {

  @Test
  void verifyModulithStructure() {
    ApplicationModules modules = ApplicationModules.of(Application.class);
    modules.verify();
  }
}
