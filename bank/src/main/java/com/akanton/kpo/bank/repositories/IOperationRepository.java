package com.akanton.kpo.bank.repositories;

import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.table.ITable;

import java.util.Collection;
import java.util.Date;
import javafx.util.Pair;

public interface IOperationRepository {
    public int addOperation(int accountId, int categoryId, double amount, String description, Date date);
    public void removeOperation(int id);
    public void load(ITable table);
    public ITable save();

    public Iterable<Operation> get(Collection<Integer> ids, Collection<Integer> accountIds, Collection<Integer> categoryIds, Collection<Pair<Double, Double>> amounts, Collection<Pair<Date, Date>> dates);
}
