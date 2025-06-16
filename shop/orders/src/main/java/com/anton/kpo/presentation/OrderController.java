package com.anton.kpo.presentation;

import com.anton.kpo.application.implementations.OrderService;
import com.anton.kpo.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public Long createOrder(@RequestParam Long userId, @RequestParam Long amount, @RequestParam String description) {
        Long id = orderService.createOrder(userId, amount, description);
        return id;
    }

    @GetMapping("/orders/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrders(userId);
    }

    @GetMapping("/order/status/{orderId}")
    public String getOrderStatus(@PathVariable Long orderId) throws Exception {
        return orderService.getOrderStatus(orderId).name();
    }
}
