package com.anton.kpo.application.implementations;

import com.anton.kpo.domain.Order;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import com.anton.kpo.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderOutbox orderOutbox;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldSaveOrderAndPublishMessage() {
        // Arrange
        Long userId = 1L;
        Long amount = 100L;
        String description = "Test Order";
        Long expectedOrderId = 100L;

        Order orderToSave = new Order(userId, amount, description, OrderStatus.NEW);
        Order savedOrder = new Order(userId, amount, description, OrderStatus.NEW);
        savedOrder.setId(expectedOrderId); // Simulate ID being set after save

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Long actualOrderId = orderService.createOrder(userId, amount, description);

        // Assert
        assertEquals(expectedOrderId, actualOrderId);
        verify(orderRepository, times(1)).save(argThat(order ->
                order.getUserId().equals(userId) &&
                order.getAmount().equals(amount) &&
                order.getDescription().equals(description) &&
                order.getStatus().equals(OrderStatus.NEW)
        ));
        verify(orderOutbox, times(1)).save(argThat(message ->
                message.getOrderId().equals(expectedOrderId) &&
                message.getUserId().equals(userId) &&
                message.getAmount().equals(amount)
        ));
    }

    @Test
    void getOrders_shouldReturnListOfOrders() {
        // Arrange
        Long userId = 1L;
        List<Order> expectedOrders = Arrays.asList(
                new Order(userId, 50L, "Order 1", OrderStatus.NEW),
                new Order(userId, 75L, "Order 2", OrderStatus.FINISHED)
        );
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.getOrders(userId);

        // Assert
        assertEquals(expectedOrders.size(), actualOrders.size());
        assertEquals(expectedOrders.get(0).getDescription(), actualOrders.get(0).getDescription());
        verify(orderRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getOrderStatus_shouldReturnStatus_whenOrderFound() throws Exception {
        // Arrange
        Long orderId = 1L;
        Order order = new Order(1L, 100L, "Test Order", OrderStatus.FINISHED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderStatus status = orderService.getOrderStatus(orderId);

        // Assert
        assertEquals(OrderStatus.FINISHED, status);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrderStatus_shouldThrowException_whenOrderNotFound() {
        // Arrange
        Long orderId = 99L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.getOrderStatus(orderId);
        });
        assertTrue(exception.getMessage().contains("Order not found with id: " + orderId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(orderRepository, times(1)).findById(orderId);
    }
}