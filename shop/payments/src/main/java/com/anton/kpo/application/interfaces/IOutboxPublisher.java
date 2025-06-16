package com.anton.kpo.application.interfaces;

public interface IOutboxPublisher {
    public void publishOrderStatusMessage();
}
