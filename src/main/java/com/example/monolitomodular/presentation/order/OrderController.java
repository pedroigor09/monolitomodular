package com.example.monolitomodular.presentation.order;

import com.example.monolitomodular.application.order.ConfirmOrderUseCase;
import com.example.monolitomodular.application.order.CreateOrderUseCase;
import com.example.monolitomodular.application.order.GetOrderUseCase;
import com.example.monolitomodular.application.order.dto.CreateOrderRequest;
import com.example.monolitomodular.application.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    
    public OrderController(CreateOrderUseCase createOrderUseCase,
                          GetOrderUseCase getOrderUseCase,
                          ConfirmOrderUseCase confirmOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = createOrderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        OrderResponse response = getOrderUseCase.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        List<OrderResponse> response = getOrderUseCase.findAll();
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> findByCustomerId(@PathVariable Long customerId) {
        List<OrderResponse> response = getOrderUseCase.findByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long id) {
        confirmOrderUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
