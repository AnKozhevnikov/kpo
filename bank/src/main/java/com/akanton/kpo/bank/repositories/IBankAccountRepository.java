package com.akanton.kpo.bank.repositories;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.table.ITable;
import javafx.util.Pair;

import java.util.Collection;

public interface IBankAccountRepository {
    public int addBankAccount(String name);
    public void removeBankAccount(int id);
    public void load(ITable table);
    public ITable save();

    public Iterable<BankAccount> get(Collection<Integer> ids, Collection<Pair<Double, Double>> balances);
}
