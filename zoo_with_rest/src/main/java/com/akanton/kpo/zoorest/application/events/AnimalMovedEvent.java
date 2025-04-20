package com.akanton.kpo.zoorest.application.events;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

public class AnimalMovedEvent extends ApplicationEvent {
    private final Object source;
    private final int animalId;
    private final int fromEnclosureId;
    private final int toEnclosureId;
    private final Date movedAt;

    public AnimalMovedEvent(Object source, int animalId, int fromEnclosureId, int toEnclosureId, Date movedAt) {
        super(source);
        this.source = source;
        this.animalId = animalId;
        this.fromEnclosureId = fromEnclosureId;
        this.toEnclosureId = toEnclosureId;
        this.movedAt = movedAt;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getFromEnclosureId() {
        return fromEnclosureId;
    }

    public int getToEnclosureId() {
        return toEnclosureId;
    }

    public Date getMovedAt() {
        return movedAt;
    }
}
