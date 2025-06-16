package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IOrderService;
import com.anton.kpo.domain.Order;
import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import com.anton.kpo.infrastructure.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderOutbox orderOutbox;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Long createOrder(Long userId, Long amount, String description) {
        Order order = new Order(userId, amount, description, OrderStatus.NEW);
        Order savedOrder = orderRepository.save(order);

        OrderPaymentMessage orderPaymentMessage = new OrderPaymentMessage(savedOrder.getId(), userId, amount);
        orderOutbox.save(orderPaymentMessage);

        return savedOrder.getId();
    }

    @Override
    public List<Order> getOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public OrderStatus getOrderStatus(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with id: " + orderId));
        return order.getStatus();
    }
}
