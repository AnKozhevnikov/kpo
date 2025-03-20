package com.akanton.kpo.bank.domain;

public class Category {
    private final int id;
    private final String name;
    private final CategoryType type;

    public Category(int id, String name, CategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public CategoryType getType() {
        return type;
    }
}
