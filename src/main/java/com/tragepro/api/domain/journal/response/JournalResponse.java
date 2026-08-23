package com.tragepro.api.domain.journal.response;

import com.tragepro.api.domain.journal.enums.TradeStatus;
import com.tragepro.api.domain.journal.enums.TradeType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponse {

    private String id;

    private String accountId;

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

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;
}
