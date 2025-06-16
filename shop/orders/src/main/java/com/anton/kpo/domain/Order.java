package com.anton.kpo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Long amount;
    private String description;
    private OrderStatus status;

    public Order() {
        // Default constructor for JPA
    }

    public Order(Long userId, Long amount, String description, OrderStatus status) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public Long getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
