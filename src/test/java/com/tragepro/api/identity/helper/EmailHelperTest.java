package com.tragepro.api.identity.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class EmailHelperTest {

  @Test
  void testEmailHelperPrivateConstructor() throws Exception {
    Constructor<EmailHelper> constructor = EmailHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      fail("Expected InvocationTargetException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getTargetException() instanceof UnsupportedOperationException);
    }
  }

  @Test
  void testEmailHelperMethods() {
    EmailHelper.sendEmail("recipient@example.com", "subject", "body");
    assertTrue(EmailHelper.sendPasswordResetEmail("recipient@example.com", "token"));
  }
}
