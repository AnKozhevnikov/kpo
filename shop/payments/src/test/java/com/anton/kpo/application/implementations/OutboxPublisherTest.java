package com.anton.kpo.application.implementations;

import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.infrastructure.messagequeue.OrderStatusProducer;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxPublisherTest {

    @Mock
    private OrderOutbox orderOutbox;

    @Mock
    private OrderStatusProducer orderPaymentProducer;

    @InjectMocks
    private OutboxPublisher outboxPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void publishOrderStatusMessage_shouldProcessAllMessagesSuccessfully() {
        // Arrange
        OrderStatusMessage message1 = mock(OrderStatusMessage.class);
        OrderStatusMessage message2 = mock(OrderStatusMessage.class);

        List<OrderStatusMessage> messages = Arrays.asList(message1, message2);

        when(orderOutbox.findByProcessedFalse()).thenReturn(messages);

        // Act
        outboxPublisher.publishOrderStatusMessage();

        // Assert
        verify(orderPaymentProducer, times(1)).sendMessage(message1);
        verify(orderPaymentProducer, times(1)).sendMessage(message2);
        
        // Verify messages are marked as processed and saved
        verify(message1, times(1)).setProcessed(true);
        verify(message2, times(1)).setProcessed(true);
        verify(orderOutbox, times(1)).save(message1);
        verify(orderOutbox, times(1)).save(message2);
    }

    @Test
    void publishOrderStatusMessage_shouldHandleSendMessageFailure() {
        // Arrange
        OrderStatusMessage message1 = mock(OrderStatusMessage.class);

        List<OrderStatusMessage> messages = Arrays.asList(message1);

        when(orderOutbox.findByProcessedFalse()).thenReturn(messages);
        doThrow(new RuntimeException("Kafka send error"))
                .when(orderPaymentProducer).sendMessage(message1);

        // Act
        outboxPublisher.publishOrderStatusMessage();

        // Assert
        verify(orderPaymentProducer, times(1)).sendMessage(message1);
        // Verify message is NOT marked as processed or saved if sending fails
        verify(message1, never()).setProcessed(true);
        verify(orderOutbox, never()).save(message1);
    }
} 