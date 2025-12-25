package com.example.monolitomodular.application.order;

import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderRepository;
import com.example.monolitomodular.domain.shared.DomainException;

/**
 * Use Case - Confirmar Pedido
 * 
 * APPLICATION LAYER:
 * - Busca o pedido
 * - Chama método de domínio para confirmar
 * - Persiste a mudança de estado
 */
public class ConfirmOrderUseCase {
    
    private final OrderRepository orderRepository;
    
    public ConfirmOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    /**
     * Confirma um pedido (transição de estado PENDING -> CONFIRMED)
     */
    public void execute(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DomainException("Pedido não encontrado: " + orderId));
        
        // Domínio valida se pode confirmar e faz a transição
        order.confirm();
        
        // Persiste o novo estado
        orderRepository.save(order);
    }
}
