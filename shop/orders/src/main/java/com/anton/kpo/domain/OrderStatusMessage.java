package com.anton.kpo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OrderStatusMessage {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    private Long orderId;
    private OrderStatus status;

    private boolean processed;

    public OrderStatusMessage(Long orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
        this.processed = false;
    }

    public OrderStatusMessage() {
        // Default constructor for JPA
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
