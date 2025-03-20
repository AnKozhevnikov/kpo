package com.akanton.kpo.bank.factories;

import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.managers.IIdManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OperationFactory {
    private IIdManager idManager;

    @Autowired
    public OperationFactory(@Qualifier("OperationIdManager") IIdManager idManager) {
        this.idManager = idManager;
    }

    public Operation createOperation(int accountId, int categoryId, double amount, String description, Date date) {
        return new Operation(idManager.getNextId(), accountId, categoryId, amount, description, date);
    }
}
