package com.example.monolitomodular.presentation.customer;

import com.example.monolitomodular.application.customer.CreateCustomerUseCase;
import com.example.monolitomodular.application.customer.GetCustomerUseCase;
import com.example.monolitomodular.application.customer.dto.CreateCustomerRequest;
import com.example.monolitomodular.application.customer.dto.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    
    public CustomerController(CreateCustomerUseCase createCustomerUseCase,
                            GetCustomerUseCase getCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
    }
    
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = createCustomerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        CustomerResponse response = getCustomerUseCase.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        List<CustomerResponse> response = getCustomerUseCase.findAll();
        return ResponseEntity.ok(response);
    }
}
