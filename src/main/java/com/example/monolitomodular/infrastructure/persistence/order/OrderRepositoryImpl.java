package com.example.monolitomodular.infrastructure.persistence.order;

import com.example.monolitomodular.domain.order.Order;
import com.example.monolitomodular.domain.order.OrderItem;
import com.example.monolitomodular.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class OrderRepositoryImpl implements OrderRepository {
    
    private final JpaOrderRepository jpaRepository;
    
    public OrderRepositoryImpl(JpaOrderRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        OrderEntity entity = jpaRepository.findByIdWithItems(id);
        return Optional.ofNullable(entity).map(this::toDomain);
    }
    
    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
    

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity(
            order.getCustomerId(),
            order.getCreatedAt(),
            order.getStatus()
        );
        
        if (order.getId() != null) {
            entity.setId(order.getId());
        }
        
        order.getItems().forEach(item -> {
            OrderItemEntity itemEntity = new OrderItemEntity(
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice()
            );
            if (item.getId() != null) {
                itemEntity.setId(item.getId());
            }
            entity.addItem(itemEntity);
        });
        
        return entity;
    }
    

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
            .map(itemEntity -> OrderItem.reconstitute(
                itemEntity.getId(),
                itemEntity.getProductName(),
                itemEntity.getQuantity(),
                itemEntity.getUnitPrice()
            ))
            .collect(Collectors.toList());
        
        return Order.reconstitute(
            entity.getId(),
            entity.getCustomerId(),
            entity.getCreatedAt(),
            entity.getStatus(),
            items
        );
    }
}
