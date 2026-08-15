package com.tragepro.api.domain.datafeed.entity;

import com.tragepro.api.common.model.entity.BaseEntity;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.Exchange;
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
@Document(collection = "watchlist")
public class WatchListEntity extends BaseEntity {
  @Id private String id;

  private String name;
  private String description;
  private Exchange exchange;
  private Set<SymbolDataModel> stocks;
}
