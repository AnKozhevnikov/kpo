package com.akanton.kpo.bank.services;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.repositories.IBankAccountRepository;
import com.akanton.kpo.bank.repositories.ICategoryRepository;
import com.akanton.kpo.bank.repositories.IOperationRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
public class BankManagerService {
    IBankAccountRepository bankAccountRepository;
    ICategoryRepository categoryRepository;
    IOperationRepository operationRepository;

    @Autowired
    public BankManagerService(IBankAccountRepository bankAccountRepository, ICategoryRepository categoryRepository, IOperationRepository operationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public int addBankAccount(String name) {
        int id = bankAccountRepository.addBankAccount(name);
        return id;
    }

    public int addCategory(String name, CategoryType type) {
        int id = categoryRepository.addCategory(name, type);
        return id;
    }

    public int addOperation(int accountId, int categoryId, double amount, String description, Date date) {
        int id = operationRepository.addOperation(accountId, categoryId, amount, description, date);
        BankAccount bankAccount = bankAccountRepository.get(Collections.singleton(accountId), null).iterator().next();
        Category category = categoryRepository.get(Collections.singleton(categoryId), null).iterator().next();
        if (category.getType() == CategoryType.INCOME) {
            bankAccount.deposit(amount);
        } else {
            bankAccount.withdraw(amount);
        }
        return id;
    }

    public boolean removeBankAccount(int id) {
        bankAccountRepository.removeBankAccount(id);
        Iterable<Operation> operations = operationRepository.get(null, Collections.singleton(id), null, null, null);
        for (Operation operation : operations) {
            operationRepository.removeOperation(operation.getId());
        }
        return true;
    }

    public boolean removeCategory(int id) {
        categoryRepository.removeCategory(id);
        Iterable<Operation> operations = operationRepository.get(null, null, Collections.singleton(id),  null, null);
        for (Operation operation : operations) {
            operationRepository.removeOperation(operation.getId());
        }
        return true;
    }

    public boolean removeOperation(int id) {
        Operation operation = operationRepository.get(Collections.singleton(id), null, null, null, null).iterator().next();
        BankAccount bankAccount = bankAccountRepository.get(Collections.singleton(operation.getAccountId()), null).iterator().next();
        Category category = categoryRepository.get(Collections.singleton(operation.getCategoryId()), null).iterator().next();
        if (category.getType() == CategoryType.INCOME) {
            bankAccount.withdraw(operation.getAmount());
        } else {
            bankAccount.deposit(operation.getAmount());
        }
        operationRepository.removeOperation(id);

        return true;
    }

    public Iterable<BankAccount> selectBankAccounts(Collection<Integer> ids, Collection<Pair<Double, Double>> balances) {
        return bankAccountRepository.get(ids, balances);
    }

    public Iterable<Operation> selectOperations(Collection<Integer> ids, Collection<Integer> accountIds, Collection<Integer> categoryIds, Collection<Pair<Double, Double>> amounts, Collection<Pair<Date, Date>> dates) {
        return operationRepository.get(ids, accountIds, categoryIds, amounts, dates);
    }

    public Iterable<Category> selectCategories(Collection<Integer> ids, Collection<CategoryType> types) {
        return categoryRepository.get(ids, types);
    }
}
