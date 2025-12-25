package com.example.monolitomodular.application.order;

import com.example.monolitomodular.application.order.dto.CreateOrderRequest;
import com.example.monolitomodular.application.order.dto.OrderItemResponse;
import com.example.monolitomodular.application.order.dto.OrderResponse;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderItem;
import com.example.monolitomodular.domain.order.OrderRepository;
import com.example.monolitomodular.domain.shared.DomainException;


public class CreateOrderUseCase {
    
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    
    public CreateOrderUseCase(OrderRepository orderRepository, 
                             CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }
    
  
    public OrderResponse execute(CreateOrderRequest request) {
        if (!customerRepository.findById(request.customerId()).isPresent()) {
            throw new DomainException("Cliente não encontrado: " + request.customerId());
        }
        
        Order order = Order.create(request.customerId());
        
        request.items().forEach(itemRequest -> {
            OrderItem item = OrderItem.create(
                itemRequest.productName(),
                itemRequest.quantity(),
                itemRequest.unitPrice()
            );
            order.addItem(item);
        });
        
        Order savedOrder = orderRepository.save(order);
        
        return toResponse(savedOrder);
    }
    

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
