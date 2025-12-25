package com.example.monolitomodular.domain.order;

import com.example.monolitomodular.domain.shared.DomainException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Order {
    
    private Long id;
    private Long customerId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItem> items;
    
    private Order() {
        this.items = new ArrayList<>();
    }
    

    public static Order create(Long customerId) {
        Order order = new Order();
        order.customerId = customerId;
        order.createdAt = LocalDateTime.now();
        order.status = OrderStatus.PENDING;
        order.validate();
        return order;
    }
    

    public static Order reconstitute(Long id, Long customerId, LocalDateTime createdAt, 
                                     OrderStatus status, List<OrderItem> items) {
        Order order = new Order();
        order.id = id;
        order.customerId = customerId;
        order.createdAt = createdAt;
        order.status = status;
        order.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        return order;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new DomainException("Não é possível adicionar itens a um pedido já confirmado");
        }
        if (item == null) {
            throw new DomainException("Item não pode ser nulo");
        }
        this.items.add(item);
    }
    

    public void confirm() {
        if (items.isEmpty()) {
            throw new DomainException("Pedido deve ter pelo menos um item");
        }
        if (status != OrderStatus.PENDING) {
            throw new DomainException("Apenas pedidos pendentes podem ser confirmados");
        }
        this.status = OrderStatus.CONFIRMED;
    }
    

    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new DomainException("Pedido não pode ser cancelado no status atual: " + status);
        }
        this.status = OrderStatus.CANCELLED;
    }
    

    public void ship() {
        if (!status.canBeShipped()) {
            throw new DomainException("Pedido não pode ser enviado no status atual: " + status);
        }
        this.status = OrderStatus.SHIPPED;
    }
    

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(OrderItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    

    private void validate() {
        if (customerId == null) {
            throw new DomainException("Cliente é obrigatório");
        }
        if (createdAt == null) {
            throw new DomainException("Data de criação é obrigatória");
        }
        if (status == null) {
            throw new DomainException("Status é obrigatório");
        }
    }
    
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { 
        return Collections.unmodifiableList(items); 
    }
    public int getItemCount() { return items.size(); }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
