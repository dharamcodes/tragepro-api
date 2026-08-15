package com.tragepro.api.common.workflow;

import static org.junit.jupiter.api.Assertions.*;

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
