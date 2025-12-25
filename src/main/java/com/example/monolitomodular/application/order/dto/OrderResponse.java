package com.example.monolitomodular.application.order.dto;

import com.example.monolitomodular.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record OrderResponse(
    Long id,
    Long customerId,
    LocalDateTime createdAt,
    OrderStatus status,
    List<OrderItemResponse> items,
    BigDecimal total
) {
}
