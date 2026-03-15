package io.tragepro.api.exception.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerErrorDto {
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private String uri;
}
