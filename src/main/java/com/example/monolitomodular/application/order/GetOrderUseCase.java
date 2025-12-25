package com.example.monolitomodular.application.order;

import com.example.monolitomodular.application.order.dto.OrderItemResponse;
import com.example.monolitomodular.application.order.dto.OrderResponse;
import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderRepository;
import com.example.monolitomodular.domain.shared.DomainException;

import java.util.List;

/**
 * Use Case - Buscar Pedidos
 * 
 * APPLICATION LAYER:
 * - Busca pedido por ID, por cliente, ou todos
 * - Converte entidades para DTOs
 */
public class GetOrderUseCase {
    
    private final OrderRepository orderRepository;
    
    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    /**
     * Busca pedido por ID
     */
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new DomainException("Pedido não encontrado: " + id));
        
        return toResponse(order);
    }
    
    /**
     * Busca pedidos de um cliente
     */
    public List<OrderResponse> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
            .map(this::toResponse)
            .toList();
    }
    
    /**
     * Busca todos os pedidos
     */
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }
    
    /**
     * Mapper: Order -> OrderResponse
     */
    private OrderResponse toResponse(Order order) {
        var itemsResponse = order.getItems().stream()
            .map(item -> new OrderItemResponse(
                item.getId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.calculateSubtotal()
            ))
            .toList();
        
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getCreatedAt(),
            order.getStatus(),
            itemsResponse,
            order.calculateTotal()
        );
    }
}
