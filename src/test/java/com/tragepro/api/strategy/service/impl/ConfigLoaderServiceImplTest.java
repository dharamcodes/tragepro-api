package com.tragepro.api.strategy.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.strategy.core.props.StrategyConfig;
import com.tragepro.api.strategy.core.props.WorkflowConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigLoaderServiceImplTest {

  private WorkflowConfig workflowConfig;
  private ConfigLoaderServiceImpl configLoaderService;

  @BeforeEach
  void setUp() {
    workflowConfig = new WorkflowConfig();
    StrategyConfig c1 = new StrategyConfig();
    c1.setName("StrategyOne");
    workflowConfig.setStrategy(List.of(c1));

    configLoaderService = new ConfigLoaderServiceImpl(workflowConfig);
  }

  @Test
  void testGetStrategyByName_Success() {
    StrategyConfig result = configLoaderService.getStrategyByName("StrategyOne");
    assertNotNull(result);
    assertEquals("StrategyOne", result.getName());
  }

  @Test
  void testGetStrategyByName_NotFound_ThrowsException() {
    AppException exception =
        assertThrows(AppException.class, () -> configLoaderService.getStrategyByName("Unknown"));
    assertEquals(ErrorType.INTERNAL_ERROR, exception.getErrorType());
  }

  @Test
  void testWorkflowConfigDirectMethods() {
    WorkflowConfig config = new WorkflowConfig();
    StrategyConfig sc = new StrategyConfig();
    sc.setName("S1");
    config.setStrategy(List.of(sc));

    assertEquals(List.of(sc), config.getStrategy());
    assertEquals(sc, config.getStrategyByName("S1"));
    assertThrows(IllegalArgumentException.class, () -> config.getStrategyByName("S2"));

    WorkflowConfig config2 = new WorkflowConfig();
    config2.setStrategy(List.of(sc));
    assertEquals(config, config2);
    assertEquals(config.hashCode(), config2.hashCode());
    assertNotNull(config.toString());
  }
}
