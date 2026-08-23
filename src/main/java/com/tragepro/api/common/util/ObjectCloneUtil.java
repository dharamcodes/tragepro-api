package com.tragepro.api.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectCloneUtil {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public <T> T clone(T object, Class<T> type) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(object), type);
        } catch (Exception e) {
            throw new AppException(ErrorType.INTERNAL_ERROR);
        }
    }
}
