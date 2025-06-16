package com.anton.kpo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    private String name;
    private Long balance;

    public Account() {
        // Default constructor for JPA
    }

    public Account(String name, Long balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Long getBalance() {
        return balance;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
