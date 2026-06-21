package com.tragepro.api.data.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import com.tragepro.api.common.model.SymbolData;
import java.util.Set;
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
@Document(collection = "watchList")
public class WatchListEntity extends BaseEntity {
  @Id private String id;

  private String name;
  private String description;
  private Set<SymbolData> stocks;
}
