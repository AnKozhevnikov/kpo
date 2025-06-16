package com.anton.kpo.application.interfaces;

import com.anton.kpo.domain.Order;
import com.anton.kpo.domain.OrderStatus;

import java.util.List;

public interface IOrderService {
    public Long createOrder(Long userId, Long amount, String description);
    public List<Order> getOrders(Long userId);
    public OrderStatus getOrderStatus(Long orderId) throws Exception;
}
