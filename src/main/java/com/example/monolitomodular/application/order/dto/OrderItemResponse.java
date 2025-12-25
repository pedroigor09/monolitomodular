package com.example.monolitomodular.application.order.dto;

import com.example.monolitomodular.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO - Item do pedido na resposta
 */
public record OrderItemResponse(
    Long id,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {
}
