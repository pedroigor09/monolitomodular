package com.example.monolitomodular.application.order.dto;

import com.example.monolitomodular.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO - Response de Pedido
 * 
 * APPLICATION LAYER:
 * - DTO completo com todos os dados do pedido
 * - Inclui lista de itens
 * - Total calculado
 */
public record OrderResponse(
    Long id,
    Long customerId,
    LocalDateTime createdAt,
    OrderStatus status,
    List<OrderItemResponse> items,
    BigDecimal total
) {
}
