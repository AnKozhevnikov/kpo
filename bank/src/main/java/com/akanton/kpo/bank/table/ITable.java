package com.akanton.kpo.bank.table;

public interface ITable extends Iterable<IRow> {
    public void addRow(IRow row);
    public Iterable<String> getHeadings();
}
