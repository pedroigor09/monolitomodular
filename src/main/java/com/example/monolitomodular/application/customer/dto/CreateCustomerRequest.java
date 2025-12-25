package com.example.monolitomodular.application.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO - Request para criar cliente
 * 
 * APPLICATION LAYER:
 * - DTOs são contratos de entrada/saída dos Use Cases
 * - Usam validações do Bean Validation (framework)
 * - Separam o domínio da apresentação
 */
public record CreateCustomerRequest(
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String name,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,
    
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    String phone
) {
}
