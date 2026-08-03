package com.tragepro.api.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.InputStream;

public class JsonReader {

  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public static <T> T readJson(String filePath, TypeReference<T> typeReference) {
    try {
      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
      if (is == null) {
        is = JsonReader.class.getClassLoader().getResourceAsStream(filePath);
      }
      if (is == null && !filePath.startsWith("/")) {
        is = JsonReader.class.getResourceAsStream("/" + filePath);
      }
      if (is == null) {
        return null;
      }
      return OBJECT_MAPPER.readValue(is, typeReference);
    } catch (Exception e) {
      return null;
    }
  }
}
