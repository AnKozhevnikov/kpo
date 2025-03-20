package com.akanton.kpo.bank.table.implementations;

import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table implements ITable {
    private List<IRow> rows;
    private List<String> headings;

    public Table(Iterable<String> headings) {
        this.rows = new ArrayList<>();
        this.headings = new ArrayList<>();
        for (String heading : headings) {
            this.headings.add(heading);
        }
    }

    @Override
    public void addRow(IRow row) {
        rows.add(row);
    }

    @Override
    public Iterable<String> getHeadings() {
        return headings;
    }

    @Override
    @NonNull
    public Iterator<IRow> iterator() {
        return new TableIterator();
    }

    private class TableIterator implements Iterator<IRow> {
        private int cur;

        public TableIterator() {
            cur = 0;
        }

        @Override
        public boolean hasNext() {
            return cur < rows.size();
        }

        @Override
        public IRow next() {
            return rows.get(cur++);
        }
    }
}
