package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IOutboxPublisher;
import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.infrastructure.messagequeue.OrderStatusProducer;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher implements IOutboxPublisher {
    @Autowired
    private OrderOutbox orderOutbox;

    @Autowired
    private OrderStatusProducer orderPaymentProducer;

    @Override
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void publishOrderStatusMessage() {
        List<OrderStatusMessage> messages = orderOutbox.findByProcessedFalse();
        for (OrderStatusMessage message : messages) {
            try {
                orderPaymentProducer.sendMessage(message);
                message.setProcessed(true);
                orderOutbox.save(message);
            } catch (Exception e) {
                System.err.println("Failed to publish message with ID: " + message.getId() + ", Error: " + e.getMessage());
            }
        }
    }
}
