package com.akanton.kpo.bank;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.services.BankManagerService;
import com.akanton.kpo.bank.services.SaveLoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class Tests {
    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Test
    public void integrationTest() {
        BankManagerService bankManagerService = context.getBean(BankManagerService.class);

        bankManagerService.addBankAccount("Bank Account 1");
        bankManagerService.addBankAccount("Bank Account 2");
        bankManagerService.addBankAccount("Bank Account 3");

        Iterable<BankAccount> accounts = bankManagerService.selectBankAccounts(null, null);

        int idBank1 = -1;
        int idBank2 = -1;
        int idBank3 = -1;
        for (BankAccount account : accounts) {
            if (account.getName().equals("Bank Account 1")) {
                idBank1 = account.getId();
            } else if (account.getName().equals("Bank Account 2")) {
                idBank2 = account.getId();
            } else if (account.getName().equals("Bank Account 3")) {
                idBank3 = account.getId();
            }
        }

        assert idBank1 != -1;
        assert idBank2 != -1;
        assert idBank3 != -1;

        bankManagerService.removeBankAccount(idBank3);
        accounts = bankManagerService.selectBankAccounts(null, null);

        boolean foundBank1 = false;
        boolean foundBank2 = false;
        boolean foundBank3 = false;
        for (BankAccount account : accounts) {
            if (account.getName().equals("Bank Account 1")) {
                foundBank1 = true;
            } else if (account.getName().equals("Bank Account 2")) {
                foundBank2 = true;
            } else if (account.getName().equals("Bank Account 3")) {
                foundBank3 = true;
            }
        }

        assert foundBank1;
        assert foundBank2;
        assert !foundBank3;

        bankManagerService.addCategory("Category 1", CategoryType.EXPENSE);
        bankManagerService.addCategory("Category 2", CategoryType.INCOME);
        bankManagerService.addCategory("Category 3", CategoryType.EXPENSE);

        int idCat1 = -1;
        int idCat2 = -1;
        int idCat3 = -1;
        Iterable<Category> categories = bankManagerService.selectCategories(null, null);

        for (Category category : categories) {
            if (category.getName().equals("Category 1")) {
                idCat1 = category.getId();
            } else if (category.getName().equals("Category 2")) {
                idCat2 = category.getId();
            } else if (category.getName().equals("Category 3")) {
                idCat3 = category.getId();
            }
        }

        assert idCat1 != -1;
        assert idCat2 != -1;
        assert idCat3 != -1;

        bankManagerService.removeCategory(idCat3);
        categories = bankManagerService.selectCategories(null, null);

        boolean foundCat1 = false;
        boolean foundCat2 = false;
        boolean foundCat3 = false;
        for (Category category : categories) {
            if (category.getName().equals("Category 1")) {
                foundCat1 = true;
            } else if (category.getName().equals("Category 2")) {
                foundCat2 = true;
            } else if (category.getName().equals("Category 3")) {
                foundCat3 = true;
            }
        }

        assert foundCat1;
        assert foundCat2;
        assert !foundCat3;

        bankManagerService.addOperation(idBank1, idCat1, 100, "Description 1", new Date(1742490532));
        bankManagerService.addOperation(idBank2, idCat2, 200, "Description 2", new Date(1742490532));
        bankManagerService.addOperation(idBank1, idCat2, 300, "Description 3", new Date(1742490532));

        Iterable<Operation> operations = bankManagerService.selectOperations(null, null, null, null, null);

        int idOp1 = -1;
        int idOp2 = -1;
        int idOp3 = -1;

        for (Operation operation : operations) {
            if (operation.getDescription().equals("Description 1")) {
                idOp1 = operation.getId();
            } else if (operation.getDescription().equals("Description 2")) {
                idOp2 = operation.getId();
            } else if (operation.getDescription().equals("Description 3")) {
                idOp3 = operation.getId();
            }
        }

        assert idOp1 != -1;
        assert idOp2 != -1;
        assert idOp3 != -1;

        bankManagerService.removeOperation(idOp3);
        operations = bankManagerService.selectOperations(null, null, null, null, null);

        boolean foundOp1 = false;
        boolean foundOp2 = false;
        boolean foundOp3 = false;

        for (Operation operation : operations) {
            if (operation.getDescription().equals("Description 1")) {
                foundOp1 = true;
            } else if (operation.getDescription().equals("Description 2")) {
                foundOp2 = true;
            } else if (operation.getDescription().equals("Description 3")) {
                foundOp3 = true;
            }
        }

        assert foundOp1;
        assert foundOp2;
        assert !foundOp3;

        SaveLoadService saveLoadService = context.getBean(SaveLoadService.class);
        saveLoadService.save("csv", "bankAccount.csv", "category.csv", "operation.csv");
        saveLoadService.load("csv", "bankAccount.csv", "category.csv", "operation.csv");

        foundBank1 = false;
        foundBank2 = false;
        accounts = bankManagerService.selectBankAccounts(null, null);
        for (BankAccount account : accounts) {
            if (account.getName().equals("Bank Account 1")) {
                foundBank1 = true;
            } else if (account.getName().equals("Bank Account 2")) {
                foundBank2 = true;
            }
        }
        assert foundBank1;
        assert foundBank2;

        foundCat1 = false;
        foundCat2 = false;
        categories = bankManagerService.selectCategories(null, null);
        for (Category category : categories) {
            if (category.getName().equals("Category 1")) {
                foundCat1 = true;
            } else if (category.getName().equals("Category 2")) {
                foundCat2 = true;
            }
        }
        assert foundCat1;
        assert foundCat2;

        foundOp1 = false;
        foundOp2 = false;
        operations = bankManagerService.selectOperations(null, null, null, null, null);
        for (Operation operation : operations) {
            if (operation.getDescription().equals("Description 1")) {
                foundOp1 = true;
            } else if (operation.getDescription().equals("Description 2")) {
                foundOp2 = true;
            }
        }
        assert foundOp1;
        assert foundOp2;

        bankManagerService.removeBankAccount(idBank1);
        bankManagerService.removeCategory(idCat2);
        operations = bankManagerService.selectOperations(null, null, null, null, null);
        assert !operations.iterator().hasNext();
    }
}
