package com.tragepro.api.common.exception.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppErrorDto {
  private String message;
  private String errorCode;
  private LocalDateTime timestamp;
}
