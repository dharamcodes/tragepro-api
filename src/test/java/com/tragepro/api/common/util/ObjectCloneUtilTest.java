package com.tragepro.api.common.util;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import org.junit.jupiter.api.Test;

class ObjectCloneUtilTest {

    static class Unserializable {
        public Object self = this;
    }

    @Test
    void testClone_Success() {
        String original = "hello";
        String cloned = ObjectCloneUtil.clone(original, String.class);
        assertEquals(original, cloned);
    }

    @Test
    void testClone_FailureThrowsAppException() {
        Unserializable obj = new Unserializable();
        AppException exception =
                assertThrows(AppException.class, () -> ObjectCloneUtil.clone(obj, Unserializable.class));
        assertEquals(ErrorType.INTERNAL_ERROR, exception.getErrorType());
    }
}
