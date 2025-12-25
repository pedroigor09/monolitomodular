package com.example.monolitomodular.infrastructure.config;

import com.example.monolitomodular.application.customer.CreateCustomerUseCase;
import com.example.monolitomodular.application.customer.GetCustomerUseCase;
import com.example.monolitomodular.application.order.ConfirmOrderUseCase;
import com.example.monolitomodular.application.order.CreateOrderUseCase;
import com.example.monolitomodular.application.order.GetOrderUseCase;
import com.example.monolitomodular.domain.customer.CustomerRepository;
import com.example.monolitomodular.domain.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {
    
    
    @Bean
    public CreateCustomerUseCase createCustomerUseCase(CustomerRepository customerRepository) {
        return new CreateCustomerUseCase(customerRepository);
    }
    
    @Bean
    public GetCustomerUseCase getCustomerUseCase(CustomerRepository customerRepository) {
        return new GetCustomerUseCase(customerRepository);
    }
    
    
    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository,
                                                 CustomerRepository customerRepository) {
        return new CreateOrderUseCase(orderRepository, customerRepository);
    }
    
    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepository orderRepository) {
        return new GetOrderUseCase(orderRepository);
    }
    
    @Bean
    public ConfirmOrderUseCase confirmOrderUseCase(OrderRepository orderRepository) {
        return new ConfirmOrderUseCase(orderRepository);
    }
}
