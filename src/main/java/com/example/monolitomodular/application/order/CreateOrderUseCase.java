package com.example.monolitomodular.application.order;

import com.example.monolitomodular.application.order.dto.CreateOrderRequest;
import com.example.monolitomodular.application.order.dto.OrderItemResponse;
import com.example.monolitomodular.application.order.dto.OrderResponse;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderItem;
import com.example.monolitomodular.domain.order.OrderRepository;
import com.example.monolitomodular.domain.shared.DomainException;

/**
 * Use Case - Criar Pedido
 * 
 * APPLICATION LAYER:
 * - Orquestra a criação de um pedido completo
 * - Valida se cliente existe
 * - Cria o pedido usando o domínio
 * - Adiciona itens ao pedido
 * - Persiste e retorna DTO
 * 
 * FLUXO:
 * 1. Valida cliente
 * 2. Cria Order (domínio)
 * 3. Cria OrderItems (domínio)
 * 4. Adiciona items ao order (regra de negócio)
 * 5. Salva no repository
 * 6. Converte para DTO
 */
public class CreateOrderUseCase {
    
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    
    public CreateOrderUseCase(OrderRepository orderRepository, 
                             CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }
    
    /**
     * Executa o caso de uso
     */
    public OrderResponse execute(CreateOrderRequest request) {
        // Regra de Aplicação: Cliente deve existir
        if (!customerRepository.findById(request.customerId()).isPresent()) {
            throw new DomainException("Cliente não encontrado: " + request.customerId());
        }
        
        // Cria pedido (domínio faz as validações)
        Order order = Order.create(request.customerId());
        
        // Adiciona itens ao pedido
        request.items().forEach(itemRequest -> {
            OrderItem item = OrderItem.create(
                itemRequest.productName(),
                itemRequest.quantity(),
                itemRequest.unitPrice()
            );
            order.addItem(item); // Regra de negócio: só adiciona se status PENDING
        });
        
        // Persiste
        Order savedOrder = orderRepository.save(order);
        
        // Converte para DTO
        return toResponse(savedOrder);
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
