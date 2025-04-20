package com.akanton.kpo.zoorest.application.events;

import org.springframework.context.ApplicationEvent;

import java.time.OffsetDateTime;

public class FeedingTimeEvent extends ApplicationEvent {
    private final Object source;
    private final int feedingScheduleId;
    private final OffsetDateTime feedingTime;

    public FeedingTimeEvent(Object source, int feedingScheduleId, OffsetDateTime feedingTime) {
        super(source);
        this.source = source;
        this.feedingScheduleId = feedingScheduleId;
        this.feedingTime = feedingTime;
    }

    public int getFeedingScheduleId() {
        return feedingScheduleId;
    }
    public OffsetDateTime getFeedingTime() { return feedingTime; }
}
