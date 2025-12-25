package com.example.monolitomodular.infrastructure.persistence.order;

import com.example.monolitomodular.domain.order.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long customerId;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();
    
    public OrderEntity(Long customerId, LocalDateTime createdAt, OrderStatus status) {
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.status = status;
    }
    
    public void addItem(OrderItemEntity item) {
        items.add(item);
        item.setOrder(this);
    }
}
