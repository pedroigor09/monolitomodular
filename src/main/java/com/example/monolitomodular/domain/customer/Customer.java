package com.example.monolitomodular.domain.customer;

import com.example.monolitomodular.domain.shared.DomainException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entity - Cliente
 * 
 * CLEAN ARCHITECTURE:
 * - Entidade do domínio com regras de negócio
 * - Validações de email, nome, etc
 * - Independente de frameworks
 */
public class Customer {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    
    private Customer() {}
    

    public static Customer create(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.validate();
        return customer;
    }
    

    public static Customer reconstitute(Long id, String name, String email, String phone) {
        Customer customer = new Customer();
        customer.id = id;
        customer.name = name;
        customer.email = email;
        customer.phone = phone;
        return customer;
    }
    
 
    public void updateInfo(String name, String email, String phone) {
        this.setName(name);
        this.setEmail(email);
        this.setPhone(phone);
        this.validate();
    }

    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("Nome é obrigatório");
        }
        if (name.length() < 3) {
            throw new DomainException("Nome deve ter pelo menos 3 caracteres");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("Email inválido");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            String cleanPhone = phone.replaceAll("[^0-9]", "");
            if (cleanPhone.length() < 10 || cleanPhone.length() > 11) {
                throw new DomainException("Telefone deve ter 10 ou 11 dígitos");
            }
        }
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    private void setName(String name) {
        this.name = name;
    }
    
    private void setEmail(String email) {
        this.email = email;
    }
    
    private void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
