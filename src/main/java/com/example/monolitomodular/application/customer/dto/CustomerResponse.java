package com.example.monolitomodular.application.customer.dto;

/**
 * DTO - Response de Cliente
 * 
 * APPLICATION LAYER:
 * - DTO de saída, retornado pelos Use Cases
 * - Não expõe a entidade do domínio diretamente
 * - Record do Java (imutável por padrão)
 */
public record CustomerResponse(
    Long id,
    String name,
    String email,
    String phone
) {
}
