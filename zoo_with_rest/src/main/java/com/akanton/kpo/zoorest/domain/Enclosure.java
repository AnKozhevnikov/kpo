package com.akanton.kpo.zoorest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;


@Entity
public class Enclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private AnimalFoodType animalFoodType;
    private AnimalHabitatType animalHabitatType;
    private int capacity;
    private int currentCount;
    private OffsetDateTime lastCleaned;

    public Enclosure() {}

    public Enclosure(AnimalFoodType animalFoodType, AnimalHabitatType animalHabitatType, int capacity) {
        this.animalFoodType = animalFoodType;
        this.animalHabitatType = animalHabitatType;
        this.capacity = capacity;
        this.currentCount = 0;
        this.lastCleaned = null;
    }

    public int getId() {
        return id;
    }

    public AnimalFoodType getAnimalFoodType() {
        return animalFoodType;
    }

    public AnimalHabitatType getAnimalHabitatType() {
        return animalHabitatType;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public OffsetDateTime getLastCleaned() {
        return lastCleaned;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnimalFoodType(AnimalFoodType animalFoodType) {
        this.animalFoodType = animalFoodType;
    }

    public void setAnimalHabitatType(AnimalHabitatType animalHabitatType) {
        this.animalHabitatType = animalHabitatType;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setLastCleaned(OffsetDateTime lastCleaned) {
        this.lastCleaned = lastCleaned;
    }

    public void clean() {
        this.lastCleaned = OffsetDateTime.now();
    }

    public void addAnimal() {
        if (currentCount < capacity) {
            currentCount++;
        } else {
            throw new IllegalStateException("Enclosure is full");
        }
    }

    public void removeAnimal() {
        if (currentCount > 0) {
            currentCount--;
        } else {
            throw new IllegalStateException("Enclosure is empty");
        }
    }
}
