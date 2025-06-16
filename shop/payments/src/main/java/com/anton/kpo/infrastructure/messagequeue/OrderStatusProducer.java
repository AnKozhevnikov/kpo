package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.OrderStatusMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusProducer {
    private final KafkaTemplate<Long, OrderStatusMessage> kafkaTemplate;

    public OrderStatusProducer(KafkaTemplate<Long, OrderStatusMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderStatusMessage message) {
        kafkaTemplate.send("order-status-topic", message.getOrderId(), message);
    }
}
