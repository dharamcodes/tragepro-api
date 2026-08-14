package com.tragepro.api.common.properties;

import com.tragepro.api.common.model.ClientHeaderModel;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "data.rest")
public class ClientConfig {

  private String clientName;

  @NotNull(message = "Base URL must not be null")
  private String baseUrl;

  @NotNull(message = "Connection timeout must not be null")
  private Integer conTimeout;

  @NotNull(message = "Read timeout must not be null")
  private Integer readTimeout;

  @NotNull(message = "Headers must not be null")
  private Set<ClientHeaderModel> clientHeaders;
}
