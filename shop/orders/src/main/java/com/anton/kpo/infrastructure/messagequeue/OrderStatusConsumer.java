package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.Order;
import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.infrastructure.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderStatusConsumer {
    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "order-status-topic", groupId = "order-status-group")
    public void listen(@Payload OrderStatusMessage message, @Header(KafkaHeaders.RECEIVED_KEY) Long key, Acknowledgment ack) {
        Long orderId = message.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with id: " + orderId));
        order.setStatus(message.getStatus());
        orderRepository.save(order);
        ack.acknowledge();
    }
}
