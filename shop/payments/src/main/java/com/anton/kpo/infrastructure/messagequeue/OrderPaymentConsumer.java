package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.infrastructure.repositories.OrderInbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentConsumer {
    @Autowired
    private OrderInbox orderInbox;

    @KafkaListener(topics = "order-payment-topic", groupId = "order-payment-group")
    public void listen(@Payload OrderPaymentMessage message, @Header(KafkaHeaders.RECEIVED_KEY) Long key, Acknowledgment ack) {
        message.setProcessed(false);
        orderInbox.save(message);
        ack.acknowledge();
    }
}
