package com.anton.kpo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String hash;

    public FileInfo() {
        // Default constructor for JPA
    }

    public FileInfo(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getHash() {
        return hash;
    }
}
