package com.example.monolitomodular.infrastructure.persistence.order;

import com.example.monolitomodular.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    

    List<OrderEntity> findByCustomerId(Long customerId);
    

    List<OrderEntity> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.id = :id")
    OrderEntity findByIdWithItems(Long id);
}
