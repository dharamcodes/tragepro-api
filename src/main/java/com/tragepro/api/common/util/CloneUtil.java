package com.tragepro.api.common.util;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CloneUtil {
  private final ObjectMapper objectMapper;

  public <T> T clone(T object, Class<T> type) {
    try {
      return objectMapper.readValue(objectMapper.writeValueAsString(object), type);
    } catch (Exception e) {
      throw new AppException(ErrorType.INTERNAL_ERROR);
    }
  }
}
