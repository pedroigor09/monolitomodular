package com.example.monolitomodular.application.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record CreateOrderRequest(
    
    @NotNull(message = "ID do cliente é obrigatório")
    Long customerId,
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid 
    List<OrderItemRequest> items
) {
}
