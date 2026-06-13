package com.tragepro.api.journal.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import com.tragepro.api.journal.model.enums.TradeStatus;
import com.tragepro.api.journal.model.enums.TradeType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "trade_journals")
public class JournalEntity extends BaseEntity {

    @Id
    private String id;

    @Indexed
    private String accountId;

    @Indexed
    private String symbol;

    private TradeType tradeType;

    private TradeStatus status;

    private BigDecimal entryPrice;

    private BigDecimal exitPrice;

    private BigDecimal quantity;

    private Instant entryTime;

    private Instant exitTime;

    private BigDecimal pnl;

    private String notes;

    private List<String> tags;
}
