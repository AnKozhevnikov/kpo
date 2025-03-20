package com.akanton.kpo.bank.domain;

import java.util.Date;

public class Operation {
    private final int id;
    private final int accountId;
    private final int categoryId;
    private final double amount;
    private final String description;
    private final Date date;

    public Operation(int id, int accountId, int categoryId, double amount, String description, Date date) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public int getAccountId() {
        return accountId;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public Date getDate() {
        return date;
    }
}
