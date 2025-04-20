package com.akanton.kpo.zoorest.application.handlers;

import com.akanton.kpo.zoorest.application.events.AnimalMovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnimalMovedEventHandler {
    @EventListener
    public void handleAnimalMovedEvent(AnimalMovedEvent event) {
        try {
            System.out.println("Animal moved event processed for animal ID: " + event.getAnimalId() + " from enclosure ID: " + event.getFromEnclosureId() + " to enclosure ID: " + event.getToEnclosureId() + " at time: " + event.getMovedAt());
        } catch (Exception e) {
            System.out.println("Error processing animal moved event: " + e.getMessage());
        }
    }
}
