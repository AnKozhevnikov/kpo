package com.akanton.kpo.bank.table.implementations;

import com.akanton.kpo.bank.table.IRow;

import java.util.HashMap;

public class Row implements IRow {
    private HashMap<String, String> row;

    public Row() {
        row = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        row.put(key, value);
    }

    @Override
    public String get(String key) {
        return row.get(key);
    }
}
