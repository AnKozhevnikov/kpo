package com.akanton.kpo.zoorest.application.handlers;

import com.akanton.kpo.zoorest.application.events.FeedingTimeEvent;
import com.akanton.kpo.zoorest.application.services.implementations.FeedingOrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FeedingTimeEventHandler {
    @Autowired
    private FeedingOrganisationService feedingOrganisationService;

    @EventListener
    public void handleFeedingTimeEvent(FeedingTimeEvent event) {
        try {
            feedingOrganisationService.feedAnimal(event.getFeedingScheduleId());
            System.out.println("Feeding time event processed for schedule ID: " + event.getFeedingScheduleId() + " at time: " + event.getFeedingTime());
        } catch (Exception e) {
            System.out.println("Error processing feeding time event: " + e.getMessage());
        }
    }
}
