package com.anton.kpo.application.implementations;

import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.infrastructure.messagequeue.OrderPaymentProducer;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxPublisherTest {

    @Mock
    private OrderOutbox orderOutbox;

    @Mock
    private OrderPaymentProducer orderPaymentProducer;

    @InjectMocks
    private OutboxPublisher outboxPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void publishOrderPaymentMessage_shouldSendAndProcessMessages() {
        // Arrange
        OrderPaymentMessage msg1 = new OrderPaymentMessage(1L, 10L, 100L);
        msg1.setProcessed(false);
        OrderPaymentMessage msg2 = new OrderPaymentMessage(2L, 20L, 200L);
        msg2.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(msg1, msg2);

        when(orderOutbox.findByProcessedFalse()).thenReturn(messages);

        // Act
        outboxPublisher.publishOrderPaymentMessage();

        // Assert
        verify(orderPaymentProducer, times(1)).sendOrderPaymentMessage(msg1);
        verify(orderPaymentProducer, times(1)).sendOrderPaymentMessage(msg2);

        assertTrue(msg1.isProcessed());
        assertTrue(msg2.isProcessed());

        verify(orderOutbox, times(1)).save(msg1);
        verify(orderOutbox, times(1)).save(msg2);
    }

    @Test
    void publishOrderPaymentMessage_shouldHandleFailureAndNotProcessMessage() {
        // Arrange
        Long orderId = 3L;
        OrderPaymentMessage msg = new OrderPaymentMessage(orderId, 30L, 300L);
        msg.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(msg);

        when(orderOutbox.findByProcessedFalse()).thenReturn(messages);
        // Simulate an exception when sending the message
        doThrow(new RuntimeException("Kafka connection failed"))
                .when(orderPaymentProducer).sendOrderPaymentMessage(msg);

        // Act
        outboxPublisher.publishOrderPaymentMessage();

        // Assert
        verify(orderPaymentProducer, times(1)).sendOrderPaymentMessage(msg);

        assertFalse(msg.isProcessed()); // Message should NOT be marked as processed

        verify(orderOutbox, never()).save(msg); // save should NOT be called for the failed message
    }
} 