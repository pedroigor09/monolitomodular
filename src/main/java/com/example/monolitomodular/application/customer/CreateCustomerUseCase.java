package com.example.monolitomodular.application.customer;

import com.example.monolitomodular.application.customer.dto.CreateCustomerRequest;
import com.example.monolitomodular.application.customer.dto.CustomerResponse;
import com.example.monolitomodular.domain.customer.Customer;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.shared.DomainException;


public class CreateCustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    

    public CustomerResponse execute(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new DomainException("Email já cadastrado: " + request.email());
        }
        
        Customer customer = Customer.create(
            request.name(),
            request.email(),
            request.phone()
        );
        
        Customer savedCustomer = customerRepository.save(customer);
        
        return toResponse(savedCustomer);
    }
    

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone()
        );
    }
}
