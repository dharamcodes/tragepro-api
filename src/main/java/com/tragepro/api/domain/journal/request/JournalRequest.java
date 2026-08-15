package com.tragepro.api.domain.journal.request;

import com.tragepro.api.domain.journal.enums.TradeStatus;
import com.tragepro.api.domain.journal.enums.TradeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class JournalRequest {

  @NotBlank(message = "Account ID is required")
  private String accountId;

  @NotBlank(message = "Symbol is required")
  private String symbol;

  @NotNull(message = "Trade type is required")
  private TradeType tradeType;

  @NotNull(message = "Trade status is required")
  private TradeStatus status;

  @NotNull(message = "Entry price is required")
  @Positive(message = "Entry price must be positive")
  private BigDecimal entryPrice;

  private BigDecimal exitPrice;

  @NotNull(message = "Quantity is required")
  @Positive(message = "Quantity must be positive")
  private BigDecimal quantity;

  @NotNull(message = "Entry time is required")
  private Instant entryTime;

  private Instant exitTime;

  private BigDecimal pnl;

  private String notes;

  private List<String> tags;
}
