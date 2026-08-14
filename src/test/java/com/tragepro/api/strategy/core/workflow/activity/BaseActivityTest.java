package com.tragepro.api.strategy.core.workflow.activity;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.workflow.BaseActivity;
import org.junit.jupiter.api.Test;

class BaseActivityTest {

  interface SampleActivity {}

  @Test
  void testLocalActivityStub_ThrowsExceptionOutsideWorkflowThread() {
    assertThrows(Error.class, () -> BaseActivity.localActivity(SampleActivity.class));
  }

  @Test
  void testGlobalActivityStub_ThrowsExceptionOutsideWorkflowThread() {
    assertThrows(Error.class, () -> BaseActivity.globalActivity(SampleActivity.class));
  }
}
