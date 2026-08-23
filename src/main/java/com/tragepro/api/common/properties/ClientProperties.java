package com.tragepro.api.common.properties;

import io.temporal.common.RetryOptions;
import lombok.Data;

@Data
public class ClientProperties {
    private String identity;
    private RetryOptions retryOptions;
}
