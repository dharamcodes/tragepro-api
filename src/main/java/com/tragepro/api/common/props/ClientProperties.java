package com.tragepro.api.common.props;

import io.temporal.common.RetryOptions;
import lombok.Data;

@Data
public class ClientProperties {
  private String identity;
  private RetryOptions retryOptions;
}
