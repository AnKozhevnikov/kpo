package com.anton.kpo.infrastructure.repositories;

import com.anton.kpo.domain.OrderStatusMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOutbox extends JpaRepository<OrderStatusMessage, Long> {
    List<OrderStatusMessage> findByProcessedFalse();
}
