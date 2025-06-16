package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IOutboxPublisher;
import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.infrastructure.messagequeue.OrderPaymentProducer;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher implements IOutboxPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);

    @Autowired
    private OrderOutbox orderOutbox;

    @Autowired
    private OrderPaymentProducer orderPaymentProducer;

    @Override
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void publishOrderPaymentMessage() {
        List<OrderPaymentMessage> messages = orderOutbox.findByProcessedFalse();

        for (OrderPaymentMessage message : messages) {
            try {
                orderPaymentProducer.sendOrderPaymentMessage(message);
                message.setProcessed(true);
                orderOutbox.save(message);
            } catch (Exception e) {
                logger.error("Failed to publish message with ID: {}", message.getId(), e);
            }
        }
    }
}
