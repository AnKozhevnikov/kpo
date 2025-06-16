package com.anton.kpo.application.interfaces;

public interface IOutboxPublisher {
    void publishOrderPaymentMessage();
}
