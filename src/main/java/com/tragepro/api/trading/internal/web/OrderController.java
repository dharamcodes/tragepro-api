package com.tragepro.api.trading.internal.web;

import com.tragepro.api.trading.TradingAdapter;
import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trading")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final TradingAdapter tradingAdapter;

  @PostMapping
  public ResponseEntity<OrderResponse> submitOrder(@Valid @RequestBody OrderRequest request) {
    return ResponseEntity.ok(tradingAdapter.submitOrder(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrderStatus(@PathVariable String id) {
    return ResponseEntity.ok(tradingAdapter.getOrderStatus(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String id) {
    return ResponseEntity.ok(tradingAdapter.cancelOrder(id));
  }
}
