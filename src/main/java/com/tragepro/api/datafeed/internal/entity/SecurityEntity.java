package com.tragepro.api.datafeed.internal.entity;

import com.tragepro.api.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "security")
public class SecurityEntity extends BaseEntity {

  @Id private String id;

  private String name;
  private String isin;
  private String symbol;
  private String symbolName;
  private String exchange;
  private String segment;
  private Integer securityId;
  private String instrument;
}
