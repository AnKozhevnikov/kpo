package com.akanton.kpo.bank.table;

public interface IRow {
    public void put(String key, String value);
    public String get(String key);
}
