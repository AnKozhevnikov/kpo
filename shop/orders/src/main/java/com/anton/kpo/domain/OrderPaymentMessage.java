package com.anton.kpo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OrderPaymentMessage {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    private Long orderId;
    private Long userId;
    private Long amount;

    private boolean processed;

    public OrderPaymentMessage() {
        // Default constructor for JPA
    }

    public OrderPaymentMessage(Long orderId, Long userId, Long amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.processed = false;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAmount() {
        return amount;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
