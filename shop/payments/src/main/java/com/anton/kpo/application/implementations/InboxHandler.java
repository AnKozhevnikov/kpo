package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IAccountService;
import com.anton.kpo.application.interfaces.IInboxHandler;
import com.anton.kpo.domain.OrderPaymentMessage;
import com.anton.kpo.domain.OrderStatus;
import com.anton.kpo.domain.OrderStatusMessage;
import com.anton.kpo.infrastructure.repositories.OrderInbox;
import com.anton.kpo.infrastructure.repositories.OrderOutbox;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboxHandler implements IInboxHandler {
    @Autowired
    private OrderInbox orderInbox;

    @Autowired
    private OrderOutbox orderOutbox;

    @Autowired
    private IAccountService accountService;

    @Override
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void handleInboxMessages() {
        List<OrderPaymentMessage> messages = orderInbox.findByProcessedFalse();

        for (OrderPaymentMessage message : messages) {
            OrderStatus status = OrderStatus.CANCELLED; // Default to CANCELLED on failure
            String errorMessage = null; // To store error details if needed, can be logged

            try {
                Long accountId = message.getUserId();
                Long amount = message.getAmount();

                boolean withdrawn = accountService.withdraw(accountId, amount);
                if (withdrawn) {
                    status = OrderStatus.FINISHED;
                } else {
                    status = OrderStatus.CANCELLED;
                    errorMessage = "Insufficient funds for user account ID: " + accountId;
                }

            } catch (Exception e) {
                // Catch any exceptions (e.g., IllegalArgumentException for "Account not found")
                status = OrderStatus.CANCELLED;
                errorMessage = "Payment processing failed: " + e.getMessage();
                System.err.println("Failed to process message with ID: " + message.getId() + ", Error: " + e.getMessage());
            } finally {
                // Always ensure an OrderStatusMessage is sent and the incoming message is marked processed
                OrderStatusMessage statusMessage = new OrderStatusMessage(message.getOrderId(), status);
                orderOutbox.save(statusMessage); // This is where the test claims it's invoked

                message.setProcessed(true);
                orderInbox.save(message);
            }
        }
    }
}
