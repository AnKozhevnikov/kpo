package com.akanton.kpo.zoorest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int amount;

    public Food() {}

    public Food(String name, int amount) {
        this.name = name;
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");
        this.amount = amount;
    }
}
