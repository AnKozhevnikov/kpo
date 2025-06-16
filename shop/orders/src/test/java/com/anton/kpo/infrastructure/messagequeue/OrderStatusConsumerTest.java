package com.anton.kpo.infrastructure.messagequeue;

import com.anton.kpo.domain.Order;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusConsumerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private OrderStatusConsumer orderStatusConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listen_shouldUpdateOrderStatusAndAcknowledge() {
        // Arrange
        Long orderId = 1L;
        OrderStatusMessage message = new OrderStatusMessage(orderId, OrderStatus.FINISHED);
        Order existingOrder = new Order(10L, 100L, "Original Order", OrderStatus.NEW);
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act
        orderStatusConsumer.listen(message, orderId, acknowledgment);

        // Assert
        assertEquals(OrderStatus.FINISHED, existingOrder.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void listen_shouldThrowException_whenOrderNotFound() {
        // Arrange
        Long orderId = 2L;
        OrderStatusMessage message = new OrderStatusMessage(orderId, OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderStatusConsumer.listen(message, orderId, acknowledgment);
        });
        assertTrue(exception.getMessage().contains("Order not found with id: " + orderId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
        verify(acknowledgment, never()).acknowledge(); // Should not acknowledge if processing fails
    }
} 