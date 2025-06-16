package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.infrastructure.repositories.OrderInbox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderPaymentConsumerTest {

    @Mock
    private OrderInbox orderInbox;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private OrderPaymentConsumer orderPaymentConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listen_shouldSaveMessageAndAcknowledge() {
        // Arrange
        Long orderId = 1L;
        Long userId = 10L;
        Long amount = 100L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);

        // Act
        orderPaymentConsumer.listen(message, orderId, acknowledgment);

        // Assert
        assertFalse(message.isProcessed()); // Should be set to false by consumer
        verify(orderInbox, times(1)).save(message);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void listen_shouldNotAcknowledge_whenSaveFails() {
        // Arrange
        Long orderId = 2L;
        Long userId = 20L;
        Long amount = 200L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);

        // Simulate an exception when saving to inbox
        doThrow(new RuntimeException("Database error"))
                .when(orderInbox).save(message);

        // Act & Assert
        // The Kafka listener framework will handle the retry, we just assert acknowledge is not called
        assertThrows(RuntimeException.class, () -> {
            orderPaymentConsumer.listen(message, orderId, acknowledgment);
        });

        assertFalse(message.isProcessed()); // Should still be false or original state
        verify(orderInbox, times(1)).save(message);
        verify(acknowledgment, never()).acknowledge(); // Should NOT acknowledge if save fails
    }
} 