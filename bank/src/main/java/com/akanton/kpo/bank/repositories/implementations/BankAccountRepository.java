package com.akanton.kpo.bank.repositories.implementations;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.factories.BankAccountFactory;
import com.akanton.kpo.bank.managers.IIdManager;
import com.akanton.kpo.bank.repositories.IBankAccountRepository;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BankAccountRepository implements IBankAccountRepository {
    @Autowired
    private BankAccountFactory factory;

    @Autowired
    @Qualifier("BankAccountIdManager")
    private IIdManager idManager;

    @Autowired
    ApplicationContext context;

    private List<BankAccount> bankAccounts;

    public BankAccountRepository() {
        this.bankAccounts = new ArrayList<>();
    }

    @Override
    public int addBankAccount(String name) {
        BankAccount bankAccount = factory.createBankAccount(name);
        bankAccounts.add(bankAccount);
        return bankAccount.getId();
    }

    @Override
    public void removeBankAccount(int id) {
        idManager.releaseId(id);
        bankAccounts.removeIf(bankAccount -> bankAccount.getId() == id);
    }

    @Override
    public void load(ITable table) {
        bankAccounts.clear();
        idManager.reset();
        for (IRow row : table) {
            BankAccount bankAccount = new BankAccount(
                    Integer.parseInt(row.get("id")),
                    row.get("name"),
                    Double.parseDouble(row.get("balance"))
            );
            bankAccounts.add(bankAccount);
            idManager.reserveId(bankAccount.getId());
        }
    }

    @Override
    public ITable save() {
        List<String> headings = Arrays.asList("id", "name", "balance");
        ITable table = context.getBean(ITable.class, headings);
        for (BankAccount bankAccount : bankAccounts) {
            IRow row = context.getBean(IRow.class);
            row.put("id", Integer.toString(bankAccount.getId()));
            row.put("name", bankAccount.getName());
            row.put("balance", Double.toString(bankAccount.getBalance()));
            table.addRow(row);
        }
        return table;
    }

    @Override
    public Iterable<BankAccount> get(Collection<Integer> ids, Collection<Pair<Double, Double>> balances) {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount bankAccount : bankAccounts) {
            if (ids != null && !ids.isEmpty() && !ids.contains(bankAccount.getId())) {
                continue;
            }
            if (balances != null && !balances.isEmpty()) {
                boolean found = false;
                for (Pair<Double, Double> balanceRange : balances) {
                    if (balanceRange.getKey() <= bankAccount.getBalance() && bankAccount.getBalance() <= balanceRange.getValue()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    continue;
                }
            }
            result.add(bankAccount);
        }

        return result;
    }
}
