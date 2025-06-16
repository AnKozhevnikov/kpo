package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.OrderPaymentMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentProducer {
    private final KafkaTemplate<Long, OrderPaymentMessage> kafkaTemplate;

    public OrderPaymentProducer(KafkaTemplate<Long, OrderPaymentMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderPaymentMessage(OrderPaymentMessage message) {
        kafkaTemplate.send("order-payment-topic", message.getOrderId(), message);
    }
}
