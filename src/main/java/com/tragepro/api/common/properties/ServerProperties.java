package com.tragepro.api.common.properties;

import lombok.Data;

@Data
public class ServerProperties {
  private String target;
  private String namespace;
  private String enableTls;
}
