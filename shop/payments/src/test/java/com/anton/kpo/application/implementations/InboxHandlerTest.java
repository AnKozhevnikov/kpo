package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IAccountService;
import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.infrastructure.repositories.OrderInbox;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboxHandlerTest {

    @Mock
    private OrderInbox orderInbox;

    @Mock
    private OrderOutbox orderOutbox;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private InboxHandler inboxHandler;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        errContent.reset(); // Clear any previous output
        System.setErr(new PrintStream(errContent, true)); // Redirect System.err for testing logs with auto-flush
    }

    @AfterEach
    void tearDown() {
        System.err.flush(); // Ensure all buffered output is written before restoring
        System.setErr(originalErr); // Restore original System.err
        try {
            errContent.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void handleInboxMessages_shouldProcessSuccessfulWithdrawal() {
        // Arrange
        Long orderId = 1L;
        Long userId = 10L;
        Long amount = 100L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);
        message.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(message);

        when(orderInbox.findByProcessedFalse()).thenReturn(messages);
        when(accountService.withdraw(userId, amount)).thenReturn(true);

        // Act
        inboxHandler.handleInboxMessages();

        // Assert
        verify(accountService, times(1)).withdraw(userId, amount);
        verify(orderOutbox, times(1)).save(argThat(statusMsg ->
                statusMsg.getOrderId().equals(orderId) &&
                statusMsg.getStatus().equals(OrderStatus.FINISHED)
        ));
        assertTrue(message.isProcessed());
        verify(orderInbox, times(1)).save(message);
        assertEquals("", errContent.toString()); // No errors should be logged
    }

    @Test
    void handleInboxMessages_shouldProcessInsufficientFunds() {
        // Arrange
        Long orderId = 2L;
        Long userId = 20L;
        Long amount = 200L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);
        message.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(message);

        when(orderInbox.findByProcessedFalse()).thenReturn(messages);
        when(accountService.withdraw(userId, amount)).thenReturn(false);

        // Act
        inboxHandler.handleInboxMessages();

        // Assert
        verify(accountService, times(1)).withdraw(userId, amount);
        verify(orderOutbox, times(1)).save(argThat(statusMsg ->
                statusMsg.getOrderId().equals(orderId) &&
                statusMsg.getStatus().equals(OrderStatus.CANCELLED)
        ));
        assertTrue(message.isProcessed());
        verify(orderInbox, times(1)).save(message);
        assertEquals("", errContent.toString()); // No errors should be logged
    }

    @Test
    void handleInboxMessages_shouldHandleAccountNotFoundException() {
        // Arrange
        Long orderId = 3L;
        Long userId = 30L;
        Long amount = 300L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);
        message.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(message);

        when(orderInbox.findByProcessedFalse()).thenReturn(messages);
        doThrow(new IllegalArgumentException("Account not found"))
                .when(accountService).withdraw(userId, amount);

        // Act
        inboxHandler.handleInboxMessages();

        // Assert
        verify(accountService, times(1)).withdraw(userId, amount);
        verify(orderOutbox, times(1)).save(argThat(statusMsg ->
                statusMsg.getOrderId().equals(orderId) &&
                statusMsg.getStatus().equals(OrderStatus.CANCELLED)
        ));
        assertTrue(message.isProcessed());
        verify(orderInbox, times(1)).save(message);

        // Verify error log output
        assertTrue(errContent.toString().contains("Failed to process message with ID: " + message.getId() + ", Error: Account not found"));
    }

    @Test
    void handleInboxMessages_shouldHandleGenericException() {
        // Arrange
        Long orderId = 4L;
        Long userId = 40L;
        Long amount = 400L;
        OrderPaymentMessage message = new OrderPaymentMessage(orderId, userId, amount);
        message.setProcessed(false);
        List<OrderPaymentMessage> messages = Arrays.asList(message);

        when(orderInbox.findByProcessedFalse()).thenReturn(messages);
        doThrow(new RuntimeException("Simulated unexpected error"))
                .when(accountService).withdraw(userId, amount);

        // Act
        inboxHandler.handleInboxMessages();

        // Assert
        verify(accountService, times(1)).withdraw(userId, amount);
        verify(orderOutbox, times(1)).save(argThat(statusMsg ->
                statusMsg.getOrderId().equals(orderId) &&
                statusMsg.getStatus().equals(OrderStatus.CANCELLED)
        ));
        assertTrue(message.isProcessed());
        verify(orderInbox, times(1)).save(message);

        // Verify error log output
        assertTrue(errContent.toString().contains("Failed to process message with ID: " + message.getId() + ", Error: Simulated unexpected error"));
    }
} 