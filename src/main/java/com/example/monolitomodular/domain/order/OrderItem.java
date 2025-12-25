package com.example.monolitomodular.domain.order;

import com.example.monolitomodular.domain.shared.DomainException;
import java.math.BigDecimal;
import java.util.Objects;


public class OrderItem {
    
    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    
    private OrderItem() {}
    

    public static OrderItem create(String productName, Integer quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.setProductName(productName);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.validate();
        return item;
    }
    

    public static OrderItem reconstitute(Long id, String productName, Integer quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.id = id;
        item.productName = productName;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        return item;
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    

    public void increaseQuantity(Integer amount) {
        if (amount <= 0) {
            throw new DomainException("Quantidade deve ser positiva");
        }
        this.quantity += amount;
    }
    

    private void validate() {
        if (productName == null || productName.trim().isEmpty()) {
            throw new DomainException("Nome do produto é obrigatório");
        }
        if (quantity == null || quantity <= 0) {
            throw new DomainException("Quantidade deve ser maior que zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Preço deve ser maior que zero");
        }
    }
    
    public Long getId() { return id; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    
    private void setProductName(String productName) {
        this.productName = productName;
    }
    
    private void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    private void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
