package com.example.ordermanagement.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer_order")// Specifies the table name for the entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")// Validates that the customer name is not blank
    private String customerName;
    private LocalDate orderDate;
    @NotBlank(message = "Shipping address is required")// Validates that the shipping address is not blank
    private String shippingAddress;
    @Positive(message = "Total must be a positive value")// Validates that the total is a positive value
    private Double total;

    // Getters and setters for the entity fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
