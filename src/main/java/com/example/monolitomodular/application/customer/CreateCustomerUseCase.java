package com.example.monolitomodular.application.customer;

import com.example.monolitomodular.application.customer.dto.CreateCustomerRequest;
import com.example.monolitomodular.application.customer.dto.CustomerResponse;
import com.example.monolitomodular.domain.customer.Customer;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.shared.DomainException;

/**
 * Use Case - Criar Cliente
 * 
 * APPLICATION LAYER:
 * - Orquestra o fluxo de criação de um cliente
 * - Valida regras de aplicação (email único)
 * - Chama o domínio para criar a entidade
 * - Persiste usando o repository
 * - Converte para DTO de resposta
 * 
 * POR QUE USE CASE?
 * - Separa a lógica de aplicação da apresentação
 * - Reutilizável (pode ser chamado de API REST, GraphQL, CLI, etc)
 * - Testável independentemente
 */
public class CreateCustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    /**
     * Executa o caso de uso
     */
    public CustomerResponse execute(CreateCustomerRequest request) {
        // Regra de Aplicação: Email deve ser único
        if (customerRepository.existsByEmail(request.email())) {
            throw new DomainException("Email já cadastrado: " + request.email());
        }
        
        // Domínio cria a entidade (com suas validações internas)
        Customer customer = Customer.create(
            request.name(),
            request.email(),
            request.phone()
        );
        
        // Persiste
        Customer savedCustomer = customerRepository.save(customer);
        
        // Converte para DTO de resposta
        return toResponse(savedCustomer);
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
