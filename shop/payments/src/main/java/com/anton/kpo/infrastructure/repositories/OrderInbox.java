package com.anton.kpo.infrastructure.repositories;

import com.anton.kpo.domain.OrderPaymentMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderInbox extends JpaRepository<OrderPaymentMessage, Long> {
    List<OrderPaymentMessage> findByProcessedFalse();
}
