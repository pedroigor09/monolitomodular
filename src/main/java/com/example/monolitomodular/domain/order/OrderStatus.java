package com.example.monolitomodular.domain.order;


public enum OrderStatus {
    PENDING,      
    CONFIRMED,    
    PREPARING,    
    SHIPPED,     
    DELIVERED,    
    CANCELLED;    
    
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
    
    public boolean canBeShipped() {
        return this == PREPARING;
    }
}
