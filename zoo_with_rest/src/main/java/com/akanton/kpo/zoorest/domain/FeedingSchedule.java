package com.akanton.kpo.zoorest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
public class FeedingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int animalId;
    private int foodId;
    private LocalTime feedingTime;
    private int amount;
    private OffsetDateTime lastFed;

    public FeedingSchedule() {}

    public FeedingSchedule(int animalId, int foodId, LocalTime feedingTime, int amount) {
        this.animalId = animalId;
        this.foodId = foodId;
        this.feedingTime = feedingTime;
        this.amount = amount;
        this.lastFed = null;
    }

    public int getId() {
        return id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getFoodId() {
        return foodId;
    }

    public LocalTime getFeedingTime() {
        return feedingTime;
    }

    public int getAmount() {
        return amount;
    }

    public OffsetDateTime getLastFed() { return lastFed; }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setFeedingTime(LocalTime feedingTime) { this.feedingTime = feedingTime; }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLastFed(OffsetDateTime lastFed) { this.lastFed = lastFed; }

    public void changeFeedingTime(LocalTime newFeedingTime) {
        this.feedingTime = newFeedingTime;
    }

    public void changeAmount(int newAmount) {
        this.amount = newAmount;
    }

    public void markFed(OffsetDateTime feedingTime) {
        this.lastFed = feedingTime;
    }
}
