package com.akanton.kpo.bank.factories;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.managers.IIdManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BankAccountFactory {
    private IIdManager idManager;

    @Autowired
    public BankAccountFactory(@Qualifier("BankAccountIdManager") IIdManager idManager) {
        this.idManager = idManager;
    }

    public BankAccount createBankAccount(String name) {
        int id = idManager.getNextId();
        return new BankAccount(id, name, 0);
    }
}
