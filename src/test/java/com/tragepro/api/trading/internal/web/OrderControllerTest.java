package com.tragepro.api.trading.internal.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tragepro.api.core.ApiTestSetup;
import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderControllerTest extends ApiTestSetup {

  @Test
  void testSubmitAndGetAndCancelOrder_Success() throws Exception {
    OrderRequest orderReq =
        new OrderRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "LIMIT", "BUY");

    String responseJson =
        mockMvc
            .perform(
                post("/api/v1/orders")
                    .header("Authorization", authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.symbol").value("AAPL"))
            .andExpect(jsonPath("$.quantity").value(10))
            .andExpect(jsonPath("$.price").value(150.0))
            .andExpect(jsonPath("$.orderType").value("LIMIT"))
            .andExpect(jsonPath("$.side").value("BUY"))
            .andExpect(jsonPath("$.status").value("SUBMITTED"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    OrderResponse submitted = objectMapper.readValue(responseJson, OrderResponse.class);

    mockMvc
        .perform(
            get("/api/v1/orders/" + submitted.id())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(submitted.id()))
        .andExpect(jsonPath("$.status").value("SUBMITTED"));

    mockMvc
        .perform(
            delete("/api/v1/orders/" + submitted.id())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(submitted.id()))
        .andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  void testGetOrderStatus_NotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/orders/non-existent")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
