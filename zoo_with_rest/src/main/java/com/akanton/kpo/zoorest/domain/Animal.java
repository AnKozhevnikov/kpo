package com.akanton.kpo.zoorest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Date;

@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private SexType sex;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private OffsetDateTime birthDate;

    private int favouriteFoodId;
    private int enclosureId;
    private boolean healthy;
    private AnimalFoodType animalFoodType;
    private AnimalHabitatType animalHabitatType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private OffsetDateTime lastFed;

    public Animal(String name, SexType sex, OffsetDateTime birthDate, int favouriteFoodId, int enclosureId, boolean healthy, AnimalFoodType animalFoodType, AnimalHabitatType animalHabitatType) {
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.favouriteFoodId = favouriteFoodId;
        this.enclosureId = enclosureId;
        this.healthy = healthy;
        this.animalFoodType = animalFoodType;
        this.animalHabitatType = animalHabitatType;
        this.lastFed = null;
    }

    public Animal() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SexType getSex() {
        return sex;
    }

    public OffsetDateTime getBirthDate() {
        return birthDate;
    }

    public int getFavouriteFoodId() {
        return favouriteFoodId;
    }

    public int getEnclosureId() {
        return enclosureId;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public AnimalFoodType getAnimalFoodType() {
        return animalFoodType;
    }

    public AnimalHabitatType getAnimalHabitatType() {
        return animalHabitatType;
    }

    public OffsetDateTime getLastFed() {
        return lastFed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public void setBirthDate(OffsetDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public void setFavouriteFoodId(int favouriteFoodId) {
        this.favouriteFoodId = favouriteFoodId;
    }

    public void setEnclosureId(int enclosureId) {
        this.enclosureId = enclosureId;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setAnimalFoodType(AnimalFoodType animalFoodType) {
        this.animalFoodType = animalFoodType;
    }

    public void setAnimalHabitatType(AnimalHabitatType animalHabitatType) {
        this.animalHabitatType = animalHabitatType;
    }

    public void setLastFed(OffsetDateTime lastFed) {
        this.lastFed = lastFed;
    }

    public void feed(OffsetDateTime feedingDate) {
        this.lastFed = feedingDate;
    }

    public void heal() {
        this.healthy = true;
    }

    public void moveToEnclosure(int newEnclosureId) {
        this.enclosureId = newEnclosureId;
    }
}
