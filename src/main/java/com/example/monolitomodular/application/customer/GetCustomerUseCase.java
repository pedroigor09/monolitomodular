package com.example.monolitomodular.application.customer;

import com.example.monolitomodular.application.customer.dto.CustomerResponse;
import com.example.monolitomodular.domain.customer.Customer;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.shared.DomainException;

import java.util.List;

/**
 * Use Case - Buscar Clientes
 * 
 * APPLICATION LAYER:
 * - Busca cliente por ID ou todos os clientes
 * - Converte entidades para DTOs
 */
public class GetCustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    /**
     * Busca cliente por ID
     */
    public CustomerResponse findById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new DomainException("Cliente não encontrado: " + id));
        
        return toResponse(customer);
    }
    
    /**
     * Busca todos os clientes
     */
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }
    
    /**
     * Mapper: Customer -> CustomerResponse
     */
    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone()
        );
    }
}
