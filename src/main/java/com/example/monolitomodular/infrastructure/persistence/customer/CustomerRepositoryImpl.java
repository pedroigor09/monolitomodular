package com.example.monolitomodular.infrastructure.persistence.customer;

import com.example.monolitomodular.domain.customer.Customer;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    
    private final JpaCustomerRepository jpaRepository;
    
    public CustomerRepositoryImpl(JpaCustomerRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(this::toDomain);
    }
    
    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    private CustomerEntity toEntity(Customer customer) {
        CustomerEntity entity = new CustomerEntity(
            customer.getName(),
            customer.getEmail(),
            customer.getPhone()
        );
        if (customer.getId() != null) {
            entity.setId(customer.getId());
        }
        return entity;
    }
    
    /**
     * Mapper: CustomerEntity (JPA) -> Customer (domínio)
     */
    private Customer toDomain(CustomerEntity entity) {
        return Customer.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone()
        );
    }
}
