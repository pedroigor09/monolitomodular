package com.example.monolitomodular.application.order;

import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderRepository;
import com.example.monolitomodular.domain.shared.DomainException;


public class ConfirmOrderUseCase {
    
    private final OrderRepository orderRepository;
    
    public ConfirmOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    

    public void execute(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DomainException("Pedido não encontrado: " + orderId));
        
        order.confirm();
        
        orderRepository.save(order);
    }
}
